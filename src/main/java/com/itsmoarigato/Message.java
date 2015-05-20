package com.itsmoarigato;

import java.util.List;


public class Message {
	private int id; 
	private User fromUser; 
	private User toUser; 
	private String subject; 
	private String contents; 
	private List<Image> images;
	public Message(int id, User fromUser, User toUser,
			String subject, String contents, List<Image> images) {
		super();
		this.id = id;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.subject = subject;
		this.contents = contents;
		this.images = images;
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
