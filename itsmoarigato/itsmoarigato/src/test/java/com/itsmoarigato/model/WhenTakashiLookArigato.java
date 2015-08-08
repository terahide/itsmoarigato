package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.itsmoarigato.Message;
import com.itsmoarigato.model.exception.IllegalMessageSendException;
import com.itsmoarigato.model.exception.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
@Transactional
@Ignore //テストが失敗する暫定措置 com.itsmoarigato.config.HttpSessionConfigのアノテーション(@EnableEmbeddedRedis,@EnableRedisHttpSession)をコメントアウトして実行してください
public class WhenTakashiLookArigato {
	
	private static final String me = Takashikun.email;
	private static final String friend = "tae@hoge.co.jp";
	private static final String not_buchos_friend = "kaori@hoge.co.jp";
	private static final String buchos_friend = "buchos_friend@hoge.co.jp";
	
	@Autowired
	Bucho bucho;

	@Autowired
	Takashikun takashi;
	
	@Autowired
	ArigatoManager arigato;

	@Autowired
	UserManager userManager;
	
	private Pagination p = new Pagination();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void before() throws IOException{
		userManager.registerUser(friend, "friend", "password");
		userManager.registerUser(buchos_friend, "buchos_friend", "password");
		link();
		clearArigato();
	}
	private void link(){
		jdbcTemplate.update("delete from friend_tbl");
		link(me,me);
		link(me,friend);
		link(me,not_buchos_friend);
		link(me,Bucho.email);
		link(Bucho.email,me);
		link(Bucho.email,friend);
		link(Bucho.email,buchos_friend);
		
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
	public void メッセージがないとき自分あてのメッセージを見ると０件であるべき(){
		List<Message> messages = arigato.getMineMessages(me,p);
		assertThat(messages.size(),is(0));
	}

	@Test
	public void 自分あてのメッセージを登録してもらい自分あてのメッセージを見ると1件であるべき(){
		bucho.sayArigato(me);
		takashi.lookArigato();
	}

	@Test
	public void 自分あてのメッセージを登録してもらいそれを消してと自分あてのメッセージを見ると0件であるべき(){
		bucho.sayArigato(me);
		
		Message message = takashi.lookArigato();
		
		bucho.deleteArigato(message.getId());
		
		takashi.lookArigatoThenNoMessage();
	}

	@Test
	public void 他人あてのメッセージを登録してもらい自分あてのメッセージを見ると0件であるべき(){
		bucho.sayArigato(friend);
		
		takashi.lookArigatoThenNoMessage();
	}

	@Test
	public void 自分あてと他人あてのメッセージを登録してもらい周囲のメッセージを見ると2件であるべき(){
		bucho.sayArigato(me);
		bucho.sayArigato(friend);
		
		List<Message> messages = arigato.getAroundMessages(me,p);
		assertThat(messages.size(),is(2));
		assertThat(messages.get(0).getToUser().getEmail(),is(friend));//新しい順
		assertThat(messages.get(1).getToUser().getEmail(),is(me));
	}

	@Test
	public void 自分あてのメッセージを変更してもらい自分あてのメッセージを見ると1件であるべき(){
		bucho.sayArigatoAndUpdate(me);
		
		List<Message> messages = arigato.getMineMessages(me,p);
		assertThat(messages.size(),is(1));

		Message message = messages.get(0);
		assertThat(message.getId(),not(0));
		assertThat(message.getFromUser().getEmail(),is(Bucho.email));
		assertThat(message.getToUser().getEmail(),is(me));
		assertThat(message.getSubject(),is("今日もありがと"));
		assertThat(message.getContents(),is("ムリしないでね:)"));
	}

	@Test
	public void 自分あてのメッセージ3件を登録してもらいある時点以降の周囲のメッセージを見ると2件であるべき(){
		bucho.sayArigato(me);
		Timestamp atPoint = new Timestamp(System.currentTimeMillis());
		wait_();
		bucho.sayArigato(me);
		bucho.sayArigato(me);
		List<Message> messages = arigato.getAroundMessages(me,new Pagination(atPoint));
		assertThat(messages.size(),is(2));
	}

	private void wait_() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// N/A 
		}
	}
	
	@Test
	public void 自分あてのメッセージ3件を登録してもらいある時点以降の自分宛てのメッセージを見ると2件であるべき(){
		bucho.sayArigato(me);
		Timestamp atPoint = new Timestamp(System.currentTimeMillis());
		wait_();
		bucho.sayArigato(me);
		bucho.sayArigato(me);
		List<Message> messages = arigato.getMineMessages(me,new Pagination(atPoint));
		for (Message message : messages) {
			System.out.print(message.getId());
			System.out.print("\t");
			System.out.print(message.getContents());
			System.out.print("\t");
			System.out.println(message.getCreated());
		}
		assertThat(messages.size(),is(2));
	}
	
	@Test
	public void 自分あての画像付きのメッセージを登録してもらい自分あてのメッセージをみると画像があるべき() throws IOException{
		bucho.sayArigatoWithImage(me);
		List<Message> messages = arigato.getMineMessages(me,new Pagination());
		Message message = messages.get(0);
		assertThat(message.getImages().size(), is(1)); 
		assertThat(message.getImages().get(0).getContents(), notNullValue());
	}
	
	@Test
	public void 存在しないメッセージを取得するとNotFoundExceptionが発生するべき(){
		expectedException.expect(NotFoundException.class);
		arigato.getMessage(me,-1);//not exist
	}
	@Test
	public void 友達以外のメッセージを取得するとNotFoundExceptionが発生するべき(){
		Message message = bucho.sayArigato(buchos_friend);
		expectedException.expect(NotFoundException.class);
		arigato.getMessage(me,message.getId());
	}
	@Test
	public void 友達以外にメッセージを登録するとIllegalMessageSendExceptionが発生するべき(){
		expectedException.expect(IllegalMessageSendException.class);
		bucho.sayArigato(not_buchos_friend);
	}
	@Test
	public void 存在しないメッセージを更新するとNotFoundExceptionが発生するべき(){
		Message message = bucho.sayArigato(buchos_friend);
		
		int noExistsArigatoId = message.getId() * 10;
		
		expectedException.expect(NotFoundException.class);
		bucho.updateArigato(noExistsArigatoId,"test","test");
	}
	@Test
	public void 他人のメッセージを更新するとIllegalMessageSendExceptionが発生すべき(){
		Message message = bucho.sayArigato(friend);
		
		expectedException.expect(IllegalMessageSendException.class);
		arigato.update(me, message.getId(), "test", "test");
	}
	
	@Test
	public void 他人のメッセージを削除するとIllegalMessageSendExceptionが発生すべき(){
		Message message = bucho.sayArigato(friend);
		
		expectedException.expect(IllegalMessageSendException.class);
		arigato.delete(me, message.getId());
	}
	
	@Test
	public void 存在しないメッセージを削除するとどうなるの(){
		Message message = bucho.sayArigato(buchos_friend);
		
		int noExistsArigatoId = message.getId() * 10;
		
		expectedException.expect(NotFoundException.class);
		bucho.deleteArigato(noExistsArigatoId);
	}
}
