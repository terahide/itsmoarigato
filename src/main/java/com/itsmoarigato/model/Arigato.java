package com.itsmoarigato.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Image;
import com.itsmoarigato.Message;
import com.itsmoarigato.User;

@Component
public class Arigato {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void add(Message message){
		jdbcTemplate.update("insert into arigato_history_tbl (from_user ,to_user ,subject ,message, created) values (?,?,?,?,sysdate)", new Object[]{message.getFromUser().getEmail(),message.getToUser().getEmail(),message.getSubject(),message.getContents()});
		Integer historyId = jdbcTemplate.queryForObject("select max(id) from arigato_history_tbl where to_user = ?", Integer.class,new Object[]{message.getToUser().getEmail()});
		jdbcTemplate.update("insert into arigato_tbl (created) values (sysdate)");
		Integer arigatoId = jdbcTemplate.queryForObject("select max(id) from arigato_tbl", Integer.class);
		jdbcTemplate.update("update arigato_history_tbl set arigato_id = ? where id = ?", new Object[]{arigatoId,historyId});
	}

	public List<Message> getMineMessages(String toUser) {
		return jdbcTemplate.query("select * from arigato_tbl", new RowMapper<Message>(){
			@Override
			public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
				User fromUser = null; 
				User toUser = null;
				String subject = null; 
				String contents = null; 
				List<Image> images = null;
				return new Message(fromUser, toUser, subject, contents, images);
			}
		});
	}
}
