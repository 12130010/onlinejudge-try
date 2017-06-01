package onlinejudge.judgehost.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import onlinejudge.judgehost.model.Message;
import onlinejudge.judgehost.service.MessageInboxService;


@Controller
public class ChatboxController {
	@Autowired
	MessageInboxService messageInboxService;
	
    @MessageMapping("/chat")
    @SendTo("/topic/chatforward")
    public Message greeting(Message message) throws Exception {
    	messageInboxService.addMessage(message);
        return message;
    }
    @SubscribeMapping("/firstJoin")
    public List<Message> initSubscribe(){
    	return messageInboxService.getListMessage();
    }
    @RequestMapping("/backup")
    public @ResponseBody String backup(){
    	try {
			messageInboxService.saveListMessage();
			return "OK";
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    @RequestMapping("/chat")
    public String chat(@RequestParam String name,@RequestParam String shortName, Model model){
    	model.addAttribute("name", name);
    	model.addAttribute("shortName", shortName);
    	return "chat";
    }
    @RequestMapping("/infoWebSocket")
    public @ResponseBody Map<String, Object> infoWebSocket(){
    	Map<String, Object> res = new HashMap<>();
    	return res;
    }
}
