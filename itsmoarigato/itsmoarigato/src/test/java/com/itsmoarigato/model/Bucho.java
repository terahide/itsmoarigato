package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
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
	Arigato arigato;
	
	public void sayArigato(String toUser) {
		arigato.add(createMessage(toUser));
	}

	private Message createMessage(String toUser) {
		Message message = new Message(toUser(email), toUser(toUser), "いつもありがと", "今日もがんばってるね:)", new ArrayList<Image>());
		return message;
	}

	private Message createMessage(int id) {
		Message message = new Message(id,toUser(null), toUser(null), "今日もありがと", "ムリしないでね:)", null, new ArrayList<Image>());
		return message;
	}

	private User toUser(String email) {
		return new User(email, "");
	}

	public void sayArigatoAndUpdate(String toUser) {
		arigato.add(createMessage(toUser));
		//機械的な都合
		Message message = getMostNewMessage(toUser);
		
		int messageId = message.getId();
		message = arigato.getMessage(messageId);
		assertThat(message.getId(), is(messageId));

		message = createMessage(messageId);
		
		arigato.update(message.getId(), message.getSubject(), message.getContents());
	}

	private Message getMostNewMessage(String toUser) {
		List<Message> messages = arigato.getMineMessages(toUser,new Pagination());
		
		//validate
		//assertThat(messages.size(), is(1));
		
		return messages.get(messages.size()-1);
	}
}
