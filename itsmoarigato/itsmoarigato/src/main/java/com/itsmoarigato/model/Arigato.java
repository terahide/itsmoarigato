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
import com.itsmoarigato.model.exception.NotFoundException;

@Component
public class Arigato {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int add(Message message){
		//FIXME friend以外は見えないようにしないとね
		int arigatoId = saveArigato(message);
		saveHistory(arigatoId,message.getSubject(),message.getContents());
		//TODO 画像の扱い
		
		return arigatoId;
	}

	private int saveArigato(Message message) {
		jdbcTemplate.update("insert into arigato_tbl (from_user ,to_user ,created) values (?,?,?)",
				new Object[]{message.getFromUser().getEmail(),message.getToUser().getEmail(),new Timestamp(System.currentTimeMillis())});
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
			+ "from_user ,"
			+ "to_user ,"
			+ "subject ,"
			+ "message ,"
			+ "h.created "
			+ "from arigato_tbl a "
			+ "inner join arigato_history_tbl h "
			+ "on (a.id = h.arigato_id "
			+ "and h.id = select max(id) from arigato_history_tbl where arigato_id = h.arigato_id　group by arigato_id"
			+ ") ";

	private static class ArigatoRowMapper implements RowMapper<Message>{
		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id"); 
			User fromUser = toUser(rs.getString("from_user")); 
			User toUser = toUser(rs.getString("to_user"));
			String subject = rs.getString("subject"); 
			String contents = rs.getString("message"); 
			Timestamp created = rs.getTimestamp("created");
			List<Image> images = null;
			return new Message(id,fromUser, toUser, subject, contents, created, images);
		}
	}
	
	public List<Message> getMineMessages(String me,Pagination page) {
		boolean afterAtPoint = page.hasAtPoint();
		
		StringBuilder sql = new StringBuilder();
		sql.append(select_from_arigato);
		sql.append("where to_user = ? ");
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

		return jdbcTemplate.query(sql.toString(), 
				new ArigatoRowMapper(),
				params);
	}

	public List<Message> getAroundMessages(String me,Pagination page) {
		boolean afterAtPoint = page.hasAtPoint();

		StringBuilder sql = new StringBuilder();
		sql.append(select_from_arigato);
		sql.append("where to_user in (");
		sql.append("select ");
		sql.append("friend ");
		sql.append("from friend_tbl ");
		sql.append("where me = ?");
		sql.append(") ");
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

	public List<Message> getWrittenMessages(String me,Pagination page) {
		boolean afterAtPoint = page.hasAtPoint();

		StringBuilder sql = new StringBuilder();
		sql.append(select_from_arigato);
		sql.append("where from_user = ?");
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

	private static User toUser(String email) {
		return new User(email, "");//TODO nameの取得
	}

	public Message getMessage(int messageId) {
		try{
			//FIXME friend以外は見えないようにしないとね
			return jdbcTemplate.queryForObject(
					select_from_arigato + 
					"where a.id = ?", 
					new ArigatoRowMapper(),
					messageId);
		}catch(EmptyResultDataAccessException e){
			throw new NotFoundException(e);
		}
	}

	public void update(int arigatoId,String subject,String message) {
		//FIXME 他のユーザのメッセージは更新できないようにしないとね
		//FIXME 対象がなかった場合どうしようね
		//FIXME friend以外は見えないようにしないとね
		saveHistory(arigatoId, subject, message);
	}
	
	public void delete(int arigatoId){
		//FIXME 他のユーザのメッセージは削除できないようにしないとね
		jdbcTemplate.update("delete from arigato_tbl where id = ?", 
				arigatoId);
	}
}
