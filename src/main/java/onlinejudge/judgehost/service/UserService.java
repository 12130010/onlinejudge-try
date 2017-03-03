package onlinejudge.judgehost.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	Map<String, String> mssvAndName = new HashMap<>();
	
	public String getUserName(String userId) throws IOException{
		if(mssvAndName.isEmpty()){
			loadMapMssvAndName();
		}
		
		return mssvAndName.get(userId);
	}

	private void loadMapMssvAndName() throws IOException {
		ClassPathResource cp = new ClassPathResource("dsDH16.txt");
		InputStream inp = cp.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inp, StandardCharsets.UTF_8));
		
		String line;
		
		StringTokenizer stk;
		while((line = br.readLine()) != null){
			stk = new StringTokenizer(line, "\t");
			mssvAndName.put(stk.nextToken(), stk.nextToken());
		}
		br.close();
	}
}
