package com.itsmoarigato.model;

import java.io.IOException;

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

import com.itsmoarigato.model.exception.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
@Transactional
@Ignore //テストが失敗する暫定措置 com.itsmoarigato.config.HttpSessionConfigのアノテーション(@EnableEmbeddedRedis,@EnableRedisHttpSession)をコメントアウトして実行してください
public class WhenTakashiLookFriends {
	
	@Autowired
	Takashikun takashi;

	@Autowired
	UserManager userManager;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void before() throws IOException{
		userManager.registerUser(Takashikun.friend, "friend", "password");
		userManager.registerUser(Bucho.friend, "buchos_friend", "password");
		link();
	}
	
	private void link(){
		deleteAllFriends();
		userManager.link(Takashikun.email,Takashikun.friend);
		userManager.link(Bucho.email,Takashikun.email);
		userManager.link(Bucho.email,Bucho.friend);
	}

	private void deleteAllFriends() {
		jdbcTemplate.update("delete from friend_tbl");
		userManager.link(Takashikun.email,Takashikun.email);
	}

	@Test
	public void ぼっちのたかしくんは友達がいない(){
		deleteAllFriends();
		
		takashi.lookFriendsThenNoFriends();
	}

	@Test
	public void たかしくんが友達をみると1人いるべき(){
		takashi.lookFriendsThenOneFriends();
	}

	@Test
	public void たかしくんが自分の友達ではない部長の友達をみるとNotFoundExceptionが発生するべき(){
		expectedException.expect(NotFoundException.class);		
		takashi.自分の友達ではない人の友達を見る();
	}
}
