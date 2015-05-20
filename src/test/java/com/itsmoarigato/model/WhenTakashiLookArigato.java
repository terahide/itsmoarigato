package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itsmoarigato.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class WhenTakashiLookArigato {
	
	private String me = "takashi@hoge.com";
	
	@Test
	public void メッセージがないときたかしあてのメッセージを見ると０件であるべき(){
		Arigato arigato = new Arigato();
		List<Message> messages = arigato.getMineMessages(me);
		assertThat(messages.size(),is(0));
	}
}
