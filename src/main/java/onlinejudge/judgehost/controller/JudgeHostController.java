package onlinejudge.judgehost.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import onlinejudge.judgehost.service.ProblemService;
import onlinejudge.judgehost.service.UserService;
import onlinejudge.process.Callback;
import onlinejudge.process.MyCompiler;
import onlinejudge.process.MyJudge;

@Controller
public class JudgeHostController {
	@Value("${path.submitRootPath}")
	public String submitRootPath;;
	@Value("${path.dataRootPath}")
	public String dataRootPath ;
	@Value("${path.resultRootPath}")
	public String resultRootPath ;
	
	Log logger = LogFactory.getLog(getClass());

	@Autowired
	UserService userService;
	
	@Autowired
	ProblemService problemService;
	@RequestMapping(value = { "/", "/home" })
	public @ResponseBody String hello() throws IOException {
		logger.debug("Wellcome");
		return "Wellcome to My Judge Host!";
	}

	@RequestMapping(value = { "/submit" }, method = RequestMethod.GET)
	public String submit(String problemId, Model model) {
		if (problemId == null || problemId.equals(""))
			return "redirect:/problem";
		model.addAttribute("problemId", problemId);
		model.addAttribute("problemPath", "listProblem/"+problemId +".pdf");
		return "submit";
	}

	@RequestMapping(value = { "/submit" }, method = RequestMethod.POST)
	public String submitPost(MultipartHttpServletRequest request, Model model)
			throws IOException, InterruptedException {
		String problemId = request.getParameter("problemId");
		logger.debug("Recieve submit with problem ID is: " + problemId);
		String message = "";
		String messageAccepted = "";
		String messageWrong = "";
		String messageError = "";
		
		String record = "";
		HttpSession session = request.getSession();
		
		if (problemId == null || problemId.isEmpty()) {
			message = "Please input Product ID.\n";
		}
		MultipartFile file = request.getFile("fileSubmit");
		logger.debug("Recieve submit with file size is: " + file == null ? 0 : file.getSize());
		if (file == null || file.getSize() == 0) {
			message += "Please select java file need to be judged.";
		}
		if (message.isEmpty()) {
			long idSubmit = System.currentTimeMillis();
			String filePath = submitRootPath + "/" + idSubmit + "/" + file.getOriginalFilename();
			FileUtils.writeByteArrayToFile(new File(filePath), file.getBytes());

			final boolean[] isCompileComplete = new boolean[] { false };
			logger.debug("Compiler is starting...");
			MyCompiler myCompiler = new MyCompiler(submitRootPath + "/" + idSubmit, file.getOriginalFilename(),
					new Callback() {
						@Override
						public void complete() {
							isCompileComplete[0] = true;
							logger.debug("Compiler is complete.");
						}
					});
			myCompiler.run();

			while (!isCompileComplete[0]) {
				Thread.sleep(100);
			}

			boolean isCompileSuccess = myCompiler.isCompileSuccess();
			logger.debug("Compiler'status is: " + isCompileSuccess);
			
			record = idSubmit + "\t" + session.getAttribute("userId") +"\t" + session.getAttribute("userName") + "\t" + problemId;
			
			if (isCompileSuccess) {

				final boolean[] isJudgeComplete = new boolean[] { false };
				logger.debug("Judge host is starting...");
				MyJudge myJudge = new MyJudge(submitRootPath + "/" + idSubmit, dataRootPath + "/" + problemId,
						getFileNameWithoutExtension(file.getOriginalFilename()), "input.txt", "output.txt", 10000,
						new Callback() {
							public void complete() {
								isJudgeComplete[0] = true;
								logger.debug("Judge host is complete.");
							}
						});
				myJudge.run();
				while (!isJudgeComplete[0]) {
					Thread.sleep(100);
				}
				// message = myJudge.getInformation();
				if (myJudge.isCorrect()) {
					messageAccepted = "Accepted.";
					record += "\tAccepted";
				} else if (myJudge.isIncorrect()) {
					messageWrong = "Wrong answer.";
					record += "\tWrong answer.";
				} else if (myJudge.isError()) {
					messageError = "There are error when run your code. Please check again!\nError is: " + myJudge.getErrorMessage();
					record += "\tError\t" + myJudge.getErrorMessage();
				}
				logger.debug("Judge host'information is: " + myJudge.getInformation());
			} else {
				message = "Compiler fail";
				record += "\tCompiler fail\t";
			}
		}
		
		FileUtils.writeStringToFile(new File(resultRootPath +"/result.txt"), record+"\n", true);
		model.addAttribute("message", message);
		model.addAttribute("messageAccepted", messageAccepted);
		model.addAttribute("messageWrong", messageWrong);
		model.addAttribute("messageError", messageError);
		model.addAttribute("problemId", problemId);
		model.addAttribute("problemPath", "listProblem/"+problemId +".pdf");
		return "submit";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginGet(HttpSession session) {
		if (session.getAttribute("userName") == null) {
			return "login";
		} else {
			return "redirect:/submit";
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(String userId, HttpSession session, Model model) throws IOException {
		String userName = userService.getUserName(userId);
		char shortName = findShortName(userName);
		String errorMessage = "";
		if (userName == null) {
			errorMessage = "Student ID not correct";
			model.addAttribute("errorMessage", errorMessage);
			return "login";
		} else {
			session.setAttribute("userId", userId);
			session.setAttribute("userName", userName);
			session.setAttribute("shortName", shortName);
			return "redirect:/submit";
		}
	}

	@RequestMapping(value = "/problem", method = RequestMethod.GET)
	public String problemGet(HttpSession session, Model model) {
		model.addAttribute("listProblem", problemService.getAllTestCase());
		return "problem";
	}

	private String getFileNameWithoutExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}
	private char findShortName(String name){
		for (int i = name.length()-1; i >= 0; i--) {
			if(name.charAt(i) == ' ')
				return name.charAt(i+1);
		}
		return 'Z';
	}
}
