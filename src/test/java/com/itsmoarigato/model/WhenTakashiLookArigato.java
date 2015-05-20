package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itsmoarigato.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
@Transactional
public class WhenTakashiLookArigato {
	
	private static final String me = "takashi@hoge.com";
	
	@Autowired
	Bucho bucho;
	
	@Autowired
	Arigato arigato;
	
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
	}
}
