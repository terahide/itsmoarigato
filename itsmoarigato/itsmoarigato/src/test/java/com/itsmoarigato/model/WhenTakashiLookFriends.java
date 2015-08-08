package com.itsmoarigato.model;

import java.io.IOException;
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

import com.itsmoarigato.User;

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

	@Before
	public void before() throws IOException{
		userManager.registerUser(Takashikun.friend, "friend", "password");
		userManager.registerUser(Bucho.friend, "buchos_friend", "password");
		link();
	}
	
	private void link(String me, String friend) {
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,sysdate)",new Object[]{me,friend});
	}

	private void link(){
		deleteAllFriends();
		link(Takashikun.email,Takashikun.email);
		link(Takashikun.email,Takashikun.friend);
		link(Bucho.email,Takashikun.email);
		link(Bucho.email,Bucho.friend);
		
	}

	@Test
	public void ぼっちのたかしくんは友達がいない(){
		deleteAllFriends();
		
		takashi.lookFriends();
	}

	private void deleteAllFriends() {
		jdbcTemplate.update("delete from friend_tbl");
	}
}
