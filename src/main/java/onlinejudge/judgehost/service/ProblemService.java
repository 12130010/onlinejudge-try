package onlinejudge.judgehost.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProblemService {
	@Value("${path.submitRootPath}")
	public String submitRootPath;;
	@Value("${path.dataRootPath}")
	public String dataRootPath ;
	
	String[] allTestCase;
	public String[] getAllTestCase(){
		if(allTestCase == null){
			File file = new File(dataRootPath);
			allTestCase = file.list();
		}
		return allTestCase;
	}
}
