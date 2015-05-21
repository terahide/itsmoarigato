package com.itsmoarigato.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itsmoarigato.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml"})
public class TableStatus {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	Arigato arigato;
	
	private static final String me = "takashi@hoge.co.jp";
	
	@Before
	public void link(){
		jdbcTemplate.update("delete from friend_tbl where me = ?",new Object[]{me});
		link(me,me);
		link(me,Bucho.email);
	}
	
	private void link(String me,String friend){
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,sysdate)",new Object[]{me,friend});
	}
	
	@Test
	public void test() {
		jdbcTemplate.update("delete from arigato_tbl　where to_user = ?",new Object[]{me});
		jdbcTemplate.query("select * from arigato_tbl", new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.print(rs.getString("id"));
				System.out.print("\t");
				System.out.print(rs.getString("from_user"));
				System.out.print("\t");
				System.out.print(rs.getString("to_user"));
				System.out.println("");
			}});
		jdbcTemplate.query("select * from arigato_history_tbl", new RowCallbackHandler(){

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				System.out.print(rs.getString("id"));
				System.out.print("\t");
				System.out.print(rs.getString("arigato_id"));
				System.out.print("\t");
				System.out.print(rs.getString("subject"));
				System.out.print("\t");
				System.out.print(rs.getString("message"));
				System.out.println("");
			}});
	}
}
