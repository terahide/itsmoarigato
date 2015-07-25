package com.itsmoarigato;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class User {
	private final String email;
	private final String name;
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
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	@JsonIgnore
	public Image getImage() {
		return image;
	}
	public URI getImageUrl() {
		try {
			return new URI("/rest/user/"+email+"/image/"+image.getId());
		} catch (URISyntaxException e) {
			throw new RuntimeException();
		}
	}
}
