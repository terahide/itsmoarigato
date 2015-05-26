package com.itsmoarigato.mvc.rest;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.itsmoarigato.User;

public class ArigatoCommand {

	int id;

	@NotBlank
	@Email
	String fromUserId; 

	@NotBlank
	@Email
	String toUserId; 

	@NotBlank
	String subject; 

	@NotBlank
	String message; 
//	List<Image> images;//TODO 後回し

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
