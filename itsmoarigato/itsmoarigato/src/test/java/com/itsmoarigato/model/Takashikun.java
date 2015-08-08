package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Message;

@Component
public class Takashikun {
	public static final String email = "takashi@hoge.co.jp";
	public static final String friend = "tae@hoge.co.jp";
	private static final String me = email;

	@Autowired
	ArigatoManager arigato;

	private Pagination p = new Pagination();

	public Message lookArigato() {
		List<Message> messages = arigato.getMineMessages(me,p);
		assertThat(messages.size(),is(1));

		Message message = messages.get(0);
		assertThat(message.getId(),not(0));
		assertThat(message.getFromUser().getEmail(),is(Bucho.email));
		assertThat(message.getToUser().getEmail(),is(me));
		assertThat(message.getSubject(),is("いつもありがと"));
		assertThat(message.getContents(),is("今日もがんばってるね:)"));
		
		return message;
	}

	public void lookArigatoThenNoMessage() {
		List<Message> messages = arigato.getMineMessages(me,p);
		assertThat(messages.size(),is(0));
	}

	public void lookArroundArigatoThenTwoMessage() {
		List<Message> messages = arigato.getAroundMessages(me,p);
		assertThat(messages.size(),is(2));
		assertThat(messages.get(0).getToUser().getEmail(),is(friend));//新しい順
		assertThat(messages.get(1).getToUser().getEmail(),is(me));
	}

	public void lookUpdatedArigato() {
		List<Message> messages = arigato.getMineMessages(me,p);
		assertThat(messages.size(),is(1));

		Message message = messages.get(0);
		assertThat(message.getId(),not(0));
		assertThat(message.getFromUser().getEmail(),is(Bucho.email));
		assertThat(message.getToUser().getEmail(),is(me));
		assertThat(message.getSubject(),is("今日もありがと"));
		assertThat(message.getContents(),is("ムリしないでね:)"));
	}

	public void lookArroundArigatoThenTwoMessage(Timestamp atPoint) {
		List<Message> messages = arigato.getAroundMessages(Takashikun.email,new Pagination(atPoint));
		assertThat(messages.size(),is(2));
	}

	public void lookMineArigatoThenTwoMessage(Timestamp atPoint) {
		List<Message> messages = arigato.getMineMessages(Takashikun.email,new Pagination(atPoint));
		assertThat(messages.size(),is(2));
	}

	public void lookArigatoWithImage() {
		List<Message> messages = arigato.getMineMessages(Takashikun.email,new Pagination());
		Message message = messages.get(0);
		assertThat(message.getImages().size(), is(1)); 
		assertThat(message.getImages().get(0).getContents(), notNullValue());
	}
}
