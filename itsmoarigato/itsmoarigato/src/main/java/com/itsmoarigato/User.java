package com.itsmoarigato;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class User {
	private final String email;
	private final String name;
	private final String password;
	private final boolean myFriend;
	private final Image image;
	public User(String email, String name, String password,boolean myFriend,Image image) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
		this.myFriend = myFriend;
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
	public boolean isMyFriend() {
		return myFriend;
	}
	public Image getImage() {
		return new Image_(image);
	}

	class Image_ extends Image{
		public Image_(Image image) {
			super(image.getId(), image.getContents());
		}
		
		public String getUrl(){
			return "/rest/user/"+email+"/image/"+getId();
		}
	}
}
