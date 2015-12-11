package util;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itsmoarigato.model.Bucho;
import com.itsmoarigato.model.UserManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TableSetuper {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserManager userManager;

	//@Ignore
	@Test
	public void remakeTable() throws IOException {
		remakeImageTable();
		remakeArigatoTable();
		remakeUserTable();
		remakeFriendTable();
	}
	
	private void link(String me,String friend){
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,?)",me,friend,new Timestamp(System.currentTimeMillis()));
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

		jdbcTemplate.execute("drop table arigato_image_Tbl if exists");
		jdbcTemplate
				.execute("create table arigato_image_Tbl("
						+ "arigato_history_id integer, image_id integer, created timestamp, primary key(arigato_history_id, image_id))");
	}
	private void remakeUserTable() throws IOException {
		jdbcTemplate.execute("drop table user_Tbl if exists");
		jdbcTemplate
				.execute("create table user_Tbl("
						+ "email char,name varchar,password char,primary key(email))");
		
		jdbcTemplate.execute("drop table user_image_Tbl if exists");
		jdbcTemplate
				.execute("create table user_image_Tbl("
						+ "email char, image_id integer, created timestamp, primary key(email, image_id))");
		registerUser();
	}
	private void remakeFriendTable() {
		jdbcTemplate.execute("drop table friend_Tbl if exists");
		jdbcTemplate
				.execute("create table friend_Tbl("
						+ "me char, friend char, created timestamp, primary key(me,friend))");
	
		link();
	}

	private void remakeImageTable() {
		jdbcTemplate.execute("drop table image_Tbl if exists");
		jdbcTemplate
				.execute("create table image_Tbl("
						+ "id serial, from_user char, contents blob, created timestamp, primary key(id))");
	}

	private static final String me = "takashi@hoge.co.jp";
	
	private void registerUser() throws IOException{
		jdbcTemplate.update("delete from user_tbl where email = ?",me);
		jdbcTemplate.update("delete from user_tbl where email = ?",Bucho.email);
		
		registerUser(me,"takashi","password");
		registerUser(Bucho.email,"bucho","password");
	}
	
	private void registerUser(String email,String name,String password) throws IOException{
		userManager.registerUser(email,name,password);
		
		File image = toImage(name);
		if(image.exists()){
			userManager.addUserImage(email, image);
		}
	}
	
	private File toImage(String name) {
		return new File("src/test/resources/persona/"+name+".jpg");
	}

	private void link(){
		jdbcTemplate.update("delete from friend_tbl where me = ?",me);
		link(me,me);
		link(me,Bucho.email);
		link(Bucho.email,Bucho.email);
	}
}
