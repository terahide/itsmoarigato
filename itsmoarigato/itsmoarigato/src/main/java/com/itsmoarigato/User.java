package com.itsmoarigato;


public class User {
	private final String email;
	private final String name;
	private final Image image;
	public User(String email, String name,Image image) {
		super();
		this.email = email;
		this.name = name;
		this.image = image;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public Image getImage() {
		return image;
	}
}
