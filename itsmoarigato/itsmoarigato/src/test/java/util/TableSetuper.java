package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.util.EncodingUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itsmoarigato.model.Bucho;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TableSetuper {
	@Autowired
	JdbcTemplate jdbcTemplate;

	//@Ignore
	@Test
	public void remakeTable() {
		remakeArigatoTable();
		remakeUserTable();
		remakeFriendTable();
	}
	
	private void link(String me,String friend){
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,?)",new Object[]{me,friend,new Timestamp(System.currentTimeMillis())});
	}

	
	private void remakeArigatoTable() {
		jdbcTemplate.execute("drop table arigato_Tbl if exists");
		jdbcTemplate
				.execute("create table arigato_Tbl("
						+ "id serial, from_user char,to_user char, created timestamp, primary key(id))");

		jdbcTemplate.execute("drop table arigato_history_Tbl if exists");
		jdbcTemplate
				.execute("create table arigato_history_Tbl("
						+ "id serial, arigato_id integer,subject text,message text, created timestamp, primary key(id))");
	}
	private void remakeUserTable() {
		jdbcTemplate.execute("drop table user_Tbl if exists");
		jdbcTemplate
				.execute("create table user_Tbl("
						+ "email char,name varchar,password char,primary key(email))");
		
		registerUser();
	}
	private void remakeFriendTable() {
		jdbcTemplate.execute("drop table friend_Tbl if exists");
		jdbcTemplate
				.execute("create table friend_Tbl("
						+ "me char, friend char, created timestamp, primary key(me,friend))");
	
		link();
	}

	private static final String me = "takashi@hoge.co.jp";
	
	private void registerUser(){
		jdbcTemplate.update("delete from user_tbl where email = ?",new Object[]{me});
		jdbcTemplate.update("delete from user_tbl where email = ?",new Object[]{Bucho.email});

		registerUser(me,"takashi",e("password"));
		registerUser(Bucho.email,"bucho",e("password"));
	}
	
	private void registerUser(String email,String name,String password){
		jdbcTemplate.update("insert into user_Tbl (email,name,password) values (?,?,?)",new Object[]{email,name,password});
	}
	
	private void link(){
		jdbcTemplate.update("delete from friend_tbl where me = ?",new Object[]{me});
		link(me,me);
		link(me,Bucho.email);
	}
	
	static String e(String s){
		return new StandardPasswordEncoder().encode(s);
	}
}
