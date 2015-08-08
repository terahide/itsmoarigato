package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Image;
import com.itsmoarigato.Message;
import com.itsmoarigato.User;

@Component
public class Bucho {

	public static final String email = "bucho@hoge.co.jp";
	
	@Autowired
	ArigatoManager arigato;
	@Autowired
	ImageManager imageManager;
	
	public Message sayArigato(String toUser) {
		arigato.add(email,createMessage(toUser));
		return getMostNewMessage(toUser);
	}

	private Message createMessage(String toUser) {
		Message message = new Message(toUser(email), toUser(toUser), "いつもありがと", "今日もがんばってるね:)", new ArrayList<Image>());
		return message;
	}

	private Message createMessage(int id) {
		Message message = new Message(id,0,toUser(null), toUser(null), "今日もありがと", "ムリしないでね:)", null, new ArrayList<Image>());
		return message;
	}

	private User toUser(String email) {
		return new User(email, "", "", null);
	}

	public void sayArigatoAndUpdate(String toUser) {
		arigato.add(email,createMessage(toUser));
		//機械的な都合
		Message message = getMostNewMessage(toUser);
		
		int messageId = message.getId();
		message = arigato.getMessage(email,messageId);
		assertThat(message.getId(), is(messageId));

		message = createMessage(messageId);
		
		arigato.update(email,message.getId(), message.getSubject(), message.getContents());
	}

	private Message getMostNewMessage(String toUser) {
		List<Message> messages = arigato.getYoursMessages(email,toUser,new Pagination());
		
		//validate
		//assertThat(messages.size(), is(1));
		
		return messages.get(messages.size()-1);
	}

	public void deleteArigato(int arigatoId) {
		arigato.delete(arigatoId);		
	}

	public void sayArigatoWithImage(String toUser) throws IOException {
		sayArigato(toUser);
		//機械的な都合
		Message message = getMostNewMessage(toUser);
		
		int historyId = message.getHistoryId();

		int imageId = imageManager.add(new FileInputStream(new File("src/test/resources/images/arigato.png")),email);
    	arigato.addImage(historyId,imageId);
	}
}
