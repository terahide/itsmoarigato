package com.itsmoarigato;

import java.util.List;


public class Message {
	private final int id; 
	private final User fromUser; 
	private final User toUser; 
	private final String subject; 
	private final String contents; 
	private final List<Image> images;

	public Message(User fromUser, User toUser,
			String subject, String contents, List<Image> images) {
		super();
		this.id = 0;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.subject = subject;
		this.contents = contents;
		this.images=images;
	}

	public Message(int id, User fromUser, User toUser,
			String subject, String contents, List<Image> images) {
		super();
		this.id = id;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.subject = subject;
		this.contents = contents;
		this.images=images;
	}
	public int getId() {
		return id;
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
	public List<Image> getImages() {
		return images;
	}
}
