package com.itsmoarigato.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Image;
import com.itsmoarigato.User;

@Component
public class UserManager {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ImageManager imageManager;
	
	public User getUser(final String email){
		return jdbcTemplate.queryForObject("select name from user_Tbl where email = ?", new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				String name = rs.getString("name");
				Integer imageId = getUserImageId(email);
				Image i; 
				if(imageId == null){
					i = null;
				}else{
					i = imageManager.findImageById(imageId);
				}
				return new User(email, name, i);
			}
			
		},email);
	}

	private Integer getUserImageId(String email) {
		try{
			return jdbcTemplate.queryForObject("select image_id from user_image_Tbl where email = ? order by created desc limit 1", new RowMapper<Integer>(){
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getInt("image_id");
				}
			},email);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
}
