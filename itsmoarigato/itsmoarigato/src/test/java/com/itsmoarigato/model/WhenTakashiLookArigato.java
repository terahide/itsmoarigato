package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
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
	
	private static final String not_buchos_friend = "kaori@hoge.co.jp";
	
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
		userManager.registerUser(Takashikun.friend, "friend", "password");
		userManager.registerUser(Bucho.friend, "buchos_friend", "password");
		link();
		clearArigato();
	}
	private void link(){
		jdbcTemplate.update("delete from friend_tbl");
		userManager.link(Takashikun.email,Takashikun.email);
		userManager.link(Takashikun.email,Takashikun.friend);
		userManager.link(Takashikun.email,not_buchos_friend);
		userManager.link(Takashikun.email,Bucho.email);
		userManager.link(Bucho.email,Takashikun.email);
		userManager.link(Bucho.email,Takashikun.friend);
		userManager.link(Bucho.email,Bucho.friend);
		
	}
	private void clearArigato(){
		jdbcTemplate.update("delete from arigato_tbl");
	}
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Test
	public void メッセージがないとき自分あてのメッセージを見ると０件であるべき(){
		List<Message> messages = arigato.getMineMessages(Takashikun.email,p);
		assertThat(messages.size(),is(0));
	}

	@Test
	public void 自分あてのメッセージを登録してもらい自分あてのメッセージを見ると1件であるべき(){
		bucho.sayArigato(Takashikun.email);
		takashi.lookArigato();
	}

	@Test
	public void 自分あてのメッセージを登録してもらいそれを消してと自分あてのメッセージを見ると0件であるべき(){
		bucho.sayArigato(Takashikun.email);
		
		Message message = takashi.lookArigato();
		
		bucho.deleteArigato(message.getId());
		
		takashi.lookArigatoThenNoMessage();
	}

	@Test
	public void 他人あてのメッセージを登録してもらい自分あてのメッセージを見ると0件であるべき(){
		bucho.sayArigato(Takashikun.friend);
		
		takashi.lookArigatoThenNoMessage();
	}

	@Test
	public void 自分あてと他人あてのメッセージを登録してもらい周囲のメッセージを見ると2件であるべき(){
		bucho.sayArigato(Takashikun.email);
		bucho.sayArigato(Takashikun.friend);

		takashi.lookArroundArigatoThenTwoMessage();
	}

	@Test
	public void 自分あてのメッセージを変更してもらい自分あてのメッセージを見ると1件であるべき(){
		bucho.sayArigatoAndUpdate(Takashikun.email);
		
		takashi.lookUpdatedArigato();
	}

	@Test
	public void 自分あてのメッセージ3件を登録してもらいある時点以降の周囲のメッセージを見ると2件であるべき(){
		bucho.sayArigato(Takashikun.email);
		Timestamp atPoint = new Timestamp(System.currentTimeMillis());
		wait_();
		bucho.sayArigato(Takashikun.email);
		bucho.sayArigato(Takashikun.email);
		
		takashi.lookArroundArigatoThenTwoMessage(atPoint);
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
		bucho.sayArigato(Takashikun.email);
		Timestamp atPoint = new Timestamp(System.currentTimeMillis());
		wait_();
		bucho.sayArigato(Takashikun.email);
		bucho.sayArigato(Takashikun.email);

		takashi.lookMineArigatoThenTwoMessage(atPoint);
	}
	
	@Test
	public void 自分あての画像付きのメッセージを登録してもらい自分あてのメッセージをみると画像があるべき() throws IOException{
		bucho.sayArigatoWithImage(Takashikun.email);
		
		takashi.lookArigatoWithImage();
	}
	
	@Test
	public void 存在しないメッセージを取得するとNotFoundExceptionが発生するべき(){
		expectedException.expect(NotFoundException.class);
		arigato.getMessage(Takashikun.email,-1);//not exist
	}
	@Test
	public void 友達以外のメッセージを取得するとNotFoundExceptionが発生するべき(){
		Message message = bucho.sayArigato(Bucho.friend);
		expectedException.expect(NotFoundException.class);
		arigato.getMessage(Takashikun.email,message.getId());
	}
	@Test
	public void 友達以外にメッセージを登録するとIllegalMessageSendExceptionが発生するべき(){
		expectedException.expect(IllegalMessageSendException.class);
		bucho.sayArigato(not_buchos_friend);
	}
	@Test
	public void 存在しないメッセージを更新するとNotFoundExceptionが発生するべき(){
		Message message = bucho.sayArigato(Bucho.friend);
		
		int noExistsArigatoId = message.getId() * 10;
		
		expectedException.expect(NotFoundException.class);
		bucho.updateArigato(noExistsArigatoId,"test","test");
	}
	@Test
	public void 他人のメッセージを更新するとIllegalMessageSendExceptionが発生すべき(){
		Message message = bucho.sayArigato(Takashikun.friend);
		
		expectedException.expect(IllegalMessageSendException.class);
		arigato.update(Takashikun.email, message.getId(), "test", "test");
	}
	
	@Test
	public void 他人のメッセージを削除するとIllegalMessageSendExceptionが発生すべき(){
		Message message = bucho.sayArigato(Takashikun.friend);
		
		expectedException.expect(IllegalMessageSendException.class);
		arigato.delete(Takashikun.email, message.getId());
	}
	
	@Test
	public void 存在しないメッセージを削除するとNotFoundExceptionが発生すべき(){
		Message message = bucho.sayArigato(Bucho.friend);
		
		int noExistsArigatoId = message.getId() * 10;
		
		expectedException.expect(NotFoundException.class);
		bucho.deleteArigato(noExistsArigatoId);
	}
}
