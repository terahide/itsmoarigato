package com.itsmoarigato;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class Message {
	private final int id;
	@JsonIgnore
	private final int historyId;
	private final User fromUser; 
	private final User toUser; 
	private final String subject; 
	private final String contents;
	private final Timestamp created;
	private final List<Image> images;

	public Message(User fromUser, User toUser,
			String subject, String contents, List<Image> images) {
		super();
		this.id = 0;
		this.historyId = 0;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.subject = subject;
		this.contents = contents;
		this.created = null;
		this.images=images;
	}

	public Message(int id, int historyId, User fromUser, User toUser,
			String subject, String contents, Timestamp created, List<Image> images) {
		super();
		this.id = id;
		this.historyId = historyId;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.subject = subject;
		this.contents = contents;
		this.created = created;
		this.images=images;
	}
	public int getId() {
		return id;
	}
	public int getHistoryId() {
		return historyId;
	}
	public User getFromUser() {
		return fromUser;
	}
	public User getToUser() {
		return toUser;
	}
	public String getSubject() {
		return subject;
	}
	public String getContents() {
		return contents;
	}
	public Timestamp getCreated() {
		return created;
	}
	public List<Image> getImages() {
		return images;
	}
}
