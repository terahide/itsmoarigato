package com.itsmoarigato.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Message;
import com.itsmoarigato.User;

@Component
public class Bucho {

	public static final String email = "bucho@hoge.com";
	
	@Autowired
	Arigato arigato;
	
	public void sayArigato(String toUser) {
		arigato.add(createMessage(toUser));
	}

	private Message createMessage(String toUser) {
		Message message = new Message(toUser(email), toUser(toUser), "いつもありがと", "今日もがんばってるね:)", new ArrayList<>());
		return message;
	}

	private User toUser(String email) {
		return new User(email, "");
	}

}
