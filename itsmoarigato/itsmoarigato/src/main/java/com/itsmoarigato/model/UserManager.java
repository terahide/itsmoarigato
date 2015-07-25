package com.itsmoarigato.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
		return jdbcTemplate.queryForObject("select name,password from user_Tbl where email = ?", new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				String name = rs.getString("name");
				String password = rs.getString("password"); 
				Integer imageId = getUserImageId(email);
				Image i; 
				if(imageId == null){
					i = null;
				}else{
					i = imageManager.findImageById(imageId);
				}
				return new User(email, name, password, i);
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
	
	public void addUserImage(String email,File image) throws IOException{
		Integer imageId = imageManager.add(new FileInputStream(image), email);
		linkImage(email,imageId);
	}
	
	private void linkImage(String email, Integer imageId) {
		jdbcTemplate.update("insert into user_image_Tbl("
				+ "email, image_id, created) values (?,?,?)",email,imageId,new Timestamp(System.currentTimeMillis()));
	}
}
