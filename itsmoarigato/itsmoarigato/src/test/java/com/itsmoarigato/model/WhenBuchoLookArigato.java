package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
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
@Ignore //テストが失敗する暫定措置 com.itsmoarigato.config.HttpSessionConfigのアノテーション(@EnableEmbeddedRedis,@EnableRedisHttpSession)をコメントアウトして実行してください
public class WhenBuchoLookArigato {

	private static final String me = Bucho.email;
	private static final String takashi = Takashikun.email;
	
	@Autowired
	Bucho bucho;

	@Autowired
	ArigatoManager arigato;
	
	private Pagination p = new Pagination();
	
	@Before
	public void before(){
		link();
		clearArigato();
	}
	private void link(){
		jdbcTemplate.update("delete from friend_tbl");
		link(me,me);
		link(me,takashi);
	}
	private void clearArigato(){
		jdbcTemplate.update("delete from arigato_tbl");
	}
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private void link(String me, String friend) {
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,sysdate)",new Object[]{me,friend});
	}

	@Test
	public void たかしあてのメッセージを登録して自分が書いたメッセージを見ると1件であるべき(){
		List<Message> messages;
		messages = arigato.getWrittenMessages(me,takashi,p);
		assertThat(messages.size(),is(0));

		bucho.sayArigato(takashi);
		
		messages = arigato.getWrittenMessages(me,me,p);
		assertThat(messages.size(),is(1));

		Message message = messages.get(0);
		assertThat(message.getId(),not(0));
		assertThat(message.getFromUser().getEmail(),is(me));
		assertThat(message.getFromUser().getImage(),notNullValue());
		assertThat(message.getToUser().getEmail(),is(takashi));
		assertThat(message.getToUser().getImage(),notNullValue());
		assertThat(message.getSubject(),is("いつもありがと"));
		assertThat(message.getContents(),is("今日もがんばってるね:)"));
	}


}
