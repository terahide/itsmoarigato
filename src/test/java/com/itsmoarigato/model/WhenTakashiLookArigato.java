package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itsmoarigato.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
@Transactional
public class WhenTakashiLookArigato {
	
	private static final String me = "takashi@hoge.com";
	private static final String friend = "tae@hoge.com";
	
	@Autowired
	Bucho bucho;
	
	@Autowired
	Arigato arigato;
	
	@Before
	public void link(){
		link(me,me);
		link(me,friend);
		link(me,Bucho.email);
	}
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private void link(String me, String friend) {
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,sysdate)",new Object[]{me,friend});
	}


	@Test
	public void メッセージがないときたかしあてのメッセージを見ると０件であるべき(){
		List<Message> messages = arigato.getMineMessages(me);
		assertThat(messages.size(),is(0));
	}

	@Test
	public void 自分あてのメッセージを登録してもらい自分あてのメッセージを見ると1件であるべき(){
		bucho.sayArigato(me);
		
		List<Message> messages = arigato.getMineMessages(me);
		assertThat(messages.size(),is(1));

		Message message = messages.get(0);
		assertThat(message.getId(),not(0));
		assertThat(message.getFromUser().getEmail(),is(Bucho.email));
		assertThat(message.getToUser().getEmail(),is(me));
		assertThat(message.getSubject(),is("いつもありがと"));
		assertThat(message.getContents(),is("今日もがんばってるね:)"));
	}

	@Test
	public void 他人あてのメッセージを登録してもらい自分あてのメッセージを見ると0件であるべき(){
		bucho.sayArigato(friend);
		
		List<Message> messages = arigato.getMineMessages(me);
		assertThat(messages.size(),is(0));
	}

	@Test
	public void 自分あてと他人あてのメッセージを登録してもらい周囲のメッセージを見ると2件であるべき(){
		bucho.sayArigato(me);
		bucho.sayArigato(friend);
		
		List<Message> messages = arigato.getArroundMessages(me);
		assertThat(messages.size(),is(2));
		assertThat(messages.get(0).getToUser().getEmail(),is(friend));//新し順
		assertThat(messages.get(1).getToUser().getEmail(),is(me));
	}
}
