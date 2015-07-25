package com.itsmoarigato;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class User {
	private final String email;
	private final String name;
	@JsonIgnore
	private final String password;
	private final Image image;
	public User(String email, String name, String password,Image image) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
		this.image = image;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public Image getImage() {
		return image;
	}
}
