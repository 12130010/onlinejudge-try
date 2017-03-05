package onlinejudge.judgehost.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import onlinejudge.judgehost.model.Message;

@Service
public class MessageInboxService {
	LinkedList<Message> listMessage;
	@Value("${chatbox.maxMessage}")
	int MAX_MESSAGE;
	
	@Value("${path.messageChatBox}")
	String messageFilePath;
	
	public List<Message> getListMessage(){
		if(listMessage == null)
			readListMessage();
		if(listMessage == null)
			listMessage = new LinkedList<>();
		return listMessage;
	}
	public void addMessage(Message message){
		if(listMessage.size() >= MAX_MESSAGE){
			listMessage.removeFirst();
		}
		listMessage.addLast(message);
	}
	public void readListMessage(){
		File fileMessage = new File(messageFilePath+"/message.bin");
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fileMessage));
			listMessage = (LinkedList<Message>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	public void saveListMessage() throws IOException{
		File fileMessage = new File(messageFilePath+"/message.bin");
		if(fileMessage.exists()){
			try {
				FileUtils.copyFile(fileMessage, new File(messageFilePath+"/message.bin" + System.currentTimeMillis()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ObjectOutputStream oos = null;
		try{
			 oos = new ObjectOutputStream(new FileOutputStream(fileMessage));
			oos.writeObject(listMessage);
			oos.flush();
			oos.close();
		} finally {
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
