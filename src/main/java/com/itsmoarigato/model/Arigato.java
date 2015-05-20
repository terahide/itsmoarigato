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
		int historyId = saveHistory(message);
		int arigatoId = saveArigato(message);
		populateArigatoIdToHistory(historyId,arigatoId);
		//TODO 画像の扱い
	}

	private int saveHistory(Message message) {
		jdbcTemplate.update("insert into arigato_history_tbl (from_user ,to_user ,subject ,message, created) values (?,?,?,?,sysdate)", new Object[]{message.getFromUser().getEmail(),message.getToUser().getEmail(),message.getSubject(),message.getContents()});
		Integer historyId = jdbcTemplate.queryForObject("select max(id) from arigato_history_tbl where to_user = ?", Integer.class,new Object[]{message.getToUser().getEmail()});
		return historyId;
	}

	//NOTE IDの取得がマルチトランザクションになるとうまくいかなくなる可能性がある。本質的に DBの排他制御（明示的ロック）で解決すべき問題。（シングルサーバならこれで問題ない）
	private synchronized int saveArigato(Message message) {
		jdbcTemplate.update("insert into arigato_tbl (created) values (sysdate)");
		Integer arigatoId = jdbcTemplate.queryForObject("select max(id) from arigato_tbl", Integer.class);
		return arigatoId;
	}

	private void populateArigatoIdToHistory(int historyId, int arigatoId) {
		jdbcTemplate.update("update arigato_history_tbl set arigato_id = ? where id = ?", new Object[]{arigatoId,historyId});
	}

	private static final String select_from_arigato = 
			"select "
			+ "a.id,"
			+ "from_user ,"
			+ "to_user ,"
			+ "subject ,"
			+ "message "
			+ "from arigato_tbl a "
			+ "inner join arigato_history_tbl h "
			+ "on (a.id = h.arigato_id "
			+ "and h.id = select max(id) from arigato_history_tbl where arigato_id = h.arigato_id"
			+ ") ";

	
	public List<Message> getMineMessages(String me) {
		return jdbcTemplate.query(
					select_from_arigato
					+ "where to_user = ? "
					+ "order by h.created　desc, h.id desc", 
				new RowMapper<Message>(){
			@Override
			public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
				int id = rs.getInt("id"); 
				User fromUser = toUser(rs.getString("from_user")); 
				User toUser = toUser(rs.getString("to_user"));
				String subject = rs.getString("subject"); 
				String contents = rs.getString("message"); 
				List<Image> images = null;
				return new Message(id,fromUser, toUser, subject, contents, images);
			}
		},
		new Object[]{me});
	}

	public List<Message> getArroundMessages(String me) {
		return jdbcTemplate.query(
				select_from_arigato
				+ "where to_user in ("
					+ "select "
					+ "friend "
					+ "from friend_tbl "
					+ "where me = ?"
					+ ") "
				+ "order by h.created　desc, h.id desc", 
			new RowMapper<Message>(){
		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id"); 
			User fromUser = toUser(rs.getString("from_user")); 
			User toUser = toUser(rs.getString("to_user"));
			String subject = rs.getString("subject"); 
			String contents = rs.getString("message"); 
			List<Image> images = null;
			return new Message(id,fromUser, toUser, subject, contents, images);
		}
	},
	new Object[]{me});
	}
	private static User toUser(String email) {
		return new User(email, "");//TODO nameの取得
	}
}
