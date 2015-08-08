package com.itsmoarigato.model;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.itsmoarigato.Image;
import com.itsmoarigato.Message;
import com.itsmoarigato.User;
import com.itsmoarigato.model.exception.IllegalMessageSendException;
import com.itsmoarigato.model.exception.NotFoundException;

@Component
public class ArigatoManager {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserManager userManager;
	@Autowired
	ImageManager imageManager;
	
	public int add(String me, Message message){
		
		String toUser = message.getToUser().getEmail();
		if( ! userManager.isFriend(me,toUser)){
			throw new IllegalMessageSendException();
		}
		
		int arigatoId = saveArigato(message);
		saveHistory(arigatoId,message.getSubject(),message.getContents());
		
		return arigatoId;
	}

	private int saveArigato(Message message) {
		jdbcTemplate.update("insert into arigato_tbl (from_user ,to_user ,created) values (?,?,?)",
				message.getFromUser().getEmail(),message.getToUser().getEmail(),new Timestamp(System.currentTimeMillis()));
		Integer arigatoId = jdbcTemplate.queryForObject("select max(id) from arigato_tbl where from_user = ? and to_user = ?", 
				Integer.class,
				message.getFromUser().getEmail(),message.getToUser().getEmail());

		return arigatoId;
	}

	private void saveHistory(int arigatoId, String subject, String message) {
		jdbcTemplate.update("insert into arigato_history_tbl (arigato_id,subject ,message, created) values (?,?,?,?)", 
				arigatoId,subject,message,new Timestamp(System.currentTimeMillis()));
	}

	//TODO VIEW にしよう。。。
	private static final String select_from_arigato = 
			"select "
			+ "a.id,"
			+ "h.id as history_id,"
			+ "from_user ,"
			+ "to_user ,"
			+ "subject ,"
			+ "message ,"
			+ "h.created "
			+ "from arigato_tbl a "
			+ "inner join arigato_history_tbl h "
			+ "on (a.id = h.arigato_id "
			+ "and h.id = select max(id) from arigato_history_tbl where arigato_id = h.arigato_id　group by arigato_id"
			+ ") "
			+ "where to_user in ("
			+ "select "
			+ "friend "
			+ "from friend_tbl "
			+ "where me = ?"
			+ ") ";

	private class ArigatoRowMapper implements RowMapper<Message>{
		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id"); 
			int historyId = rs.getInt("history_id"); 
			User fromUser = toUser(rs.getString("from_user")); 
			User toUser = toUser(rs.getString("to_user"));
			String subject = rs.getString("subject"); 
			String contents = rs.getString("message"); 
			Timestamp created = rs.getTimestamp("created");
			
			
			
			
			List<Image> images = jdbcTemplate.query("select id from image_tbl i inner join arigato_image_tbl l on (i.id = l.image_id) where l.arigato_history_id = ?",new RowMapper<Image>(){
				@Override
				public Image mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					
					return imageManager.findImageById(rs.getInt("id"));
				}
			},
			historyId);
			
			return new Message(id,historyId,fromUser, toUser, subject, contents, created, images);
		}
	}
	public List<Message> getMineMessages(String me,Pagination page) {
		return getYoursMessages(me, me, page);
	}
	
	public List<Message> getYoursMessages(String me,String you,Pagination page) {
		boolean afterAtPoint = page.hasAtPoint();
		
		StringBuilder sql = new StringBuilder();
		sql.append(select_from_arigato);
		sql.append("and to_user = ? ");
		if(afterAtPoint){
			sql.append("and h.created > ? ");
		}
		sql.append("order by h.created　desc, h.id desc ");
		sql.append("limit ? offset ? ");
		Object[] params;
		if(afterAtPoint){
			params = new Object[]{me,you,page.getAtPoint(),page.getLimit(),page.getOffset()};
		}else{
			params = new Object[]{me,you,page.getLimit(),page.getOffset()};
		}

		return jdbcTemplate.query(sql.toString(), 
				new ArigatoRowMapper(),
				params);
	}

	public List<Message> getAroundMessages(String me,Pagination page) {
		boolean afterAtPoint = page.hasAtPoint();

		StringBuilder sql = new StringBuilder();
		sql.append(select_from_arigato);
		if(afterAtPoint){
			sql.append("and h.created > ? ");
		}
		sql.append("order by h.created　desc, h.id desc ");
		sql.append("limit ? offset ? ");

		Object[] params;
		if(afterAtPoint){
			params = new Object[]{me,page.getAtPoint(),page.getLimit(),page.getOffset()};
		}else{
			params = new Object[]{me,page.getLimit(),page.getOffset()};
		}

		return jdbcTemplate.query(sql.toString() , 
				new ArigatoRowMapper(),
				params);
	}

	public List<Message> getWrittenMessages(String me,String writer,Pagination page) {
		boolean afterAtPoint = page.hasAtPoint();

		StringBuilder sql = new StringBuilder();
		sql.append(select_from_arigato);
		sql.append("and from_user = ?");
		if(afterAtPoint){
			sql.append("and h.created > ? ");
		}
		sql.append("order by h.created　desc, h.id desc ");
		sql.append("limit ? offset ? ");

		Object[] params;
		if(afterAtPoint){
			params = new Object[]{me,writer,page.getAtPoint(),page.getLimit(),page.getOffset()};
		}else{
			params = new Object[]{me,writer,page.getLimit(),page.getOffset()};
		}

		return jdbcTemplate.query(sql.toString() , 
				new ArigatoRowMapper(),
				params);
	}

	private User toUser(String email) {
		return userManager.getUser(email);
	}

	public Message getMessage(String me,int arigatoId) {
		try{
			//FIXME friend以外は見えないようにしないとね
			return jdbcTemplate.queryForObject(
					select_from_arigato + 
					"and a.id = ?", 
					new ArigatoRowMapper(),
					me,
					arigatoId);
		}catch(EmptyResultDataAccessException e){
			throw new NotFoundException(e);
		}
	}

	public void update(String me,int arigatoId,String subject,String message) {
		validateForUpdate(me,arigatoId);
		saveHistory(arigatoId, subject, message);
	}
	
	private void validateForUpdate(String me,int arigatoId) {
		Message message = getMessage(me, arigatoId);//if no exist or not frinends message then throw NotFoundException
		if( ! message.getFromUser().getEmail().equals(me)){
			throw new IllegalMessageSendException();
		}
	}

	public void delete(String me,int arigatoId){
		validateForUpdate(me,arigatoId);
		jdbcTemplate.update("delete from arigato_tbl where id = ?", 
				arigatoId);
	}

	public void addImage(int arigatoHistoryId, int imageId) {
		jdbcTemplate.update("insert into arigato_image_tbl (arigato_history_id ,image_id ,created) values (?,?,?)",
				arigatoHistoryId,
				imageId,
				new Timestamp(System.currentTimeMillis()));
	}
}
