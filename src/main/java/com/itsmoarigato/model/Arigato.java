package com.itsmoarigato.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Message;

@Component
public class Arigato {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void add(Message message){
//		jdbcTemplate.update("insert into ", args);
	}

	public List<Message> getMineMessages(String toUser) {
		return new ArrayList<>();
	}
}
