package util;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itsmoarigato.model.Bucho;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TableSetuper {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Ignore
	@Test
	public void remakeTable() {
		remakeArigatoTable();
		remakeUserTable();
		remakeFriendTable();
	}
	
	private void link(String me,String friend){
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,sysdate)",new Object[]{me,friend});
	}

	
	private void remakeArigatoTable() {
		jdbcTemplate.execute("drop table arigato_Tbl if exists");
		jdbcTemplate
				.execute("create table arigato_Tbl("
						+ "id serial, from_user char,to_user char, created datetime, primary key(id))");

		jdbcTemplate.execute("drop table arigato_history_Tbl if exists");
		jdbcTemplate
				.execute("create table arigato_history_Tbl("
						+ "id serial, arigato_id integer,subject text,message text, created datetime, primary key(id))");
	}
	private void remakeUserTable() {
		jdbcTemplate.execute("drop table user_Tbl if exists");
		jdbcTemplate
				.execute("create table user_Tbl("
						+ "email char,name varchar,password char,primary key(email))");
	}
	private void remakeFriendTable() {
		jdbcTemplate.execute("drop table friend_Tbl if exists");
		jdbcTemplate
				.execute("create table friend_Tbl("
						+ "me char, friend char, created datetime, primary key(me,friend))");
	
		link();
	}

	private static final String me = "takashi@hoge.co.jp";
	
	private void link(){
		jdbcTemplate.update("delete from friend_tbl where me = ?",new Object[]{me});
		link(me,me);
		link(me,Bucho.email);
	}
	
}
