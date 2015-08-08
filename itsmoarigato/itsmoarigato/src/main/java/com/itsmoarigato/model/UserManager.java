package com.itsmoarigato.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Image;
import com.itsmoarigato.User;
import com.itsmoarigato.model.exception.NotFoundException;

@Component
public class UserManager {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ImageManager imageManager;
	
	public void registerUser(String email,String name,String password) throws IOException{
		jdbcTemplate.update("insert into user_Tbl (email,name,password) values (?,?,?)",new Object[]{email,name,e(password)});
	}

	public User getUser(final String email){
		try{
			return jdbcTemplate.queryForObject("select email,name,password from user_Tbl where email = ?", new UserRowMapper(),email);
		}catch(EmptyResultDataAccessException e){
			throw new NotFoundException(e);
		}
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
	
	public void link(String me, String friend) {
		jdbcTemplate.update("insert into friend_tbl (me,friend,created) values (?,?,sysdate)",me,friend);
	}

	public void addUserImage(String email,File image) throws IOException{
		Integer imageId = imageManager.add(new FileInputStream(image), email);
		linkImage(email,imageId);
	}
	
	private void linkImage(String email, Integer imageId) {
		jdbcTemplate.update("insert into user_image_Tbl("
				+ "email, image_id, created) values (?,?,?)",email,imageId,new Timestamp(System.currentTimeMillis()));
	}

	static String e(String s){
		return new StandardPasswordEncoder().encode(s);
	}

	public List<User> getFriends(String email, Pagination p) {
		return jdbcTemplate.query(
				"select u.email,u.name,u.password from user_Tbl u inner join friend_tbl f on (u.email = f.friend) where f.me = ? limit ? offset ?"
				,new UserRowMapper(),
				email,
				p.getLimit(),
				p.getOffset()
				);
	}

	public boolean isFriend(String me,String toUser) {
		Integer counted = jdbcTemplate.queryForObject(
			"select "
			+ "count(*) "
			+ "from friend_tbl "
			+ "where me = ? "
			+ "and friend = ?"
			,Integer.class,
			me,
			toUser);
		return 0 < counted;
	}
	
	private class UserRowMapper implements RowMapper<User>{
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			String email = rs.getString("email");
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
	}  
}
