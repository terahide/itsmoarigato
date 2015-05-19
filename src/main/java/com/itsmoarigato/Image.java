package com.itsmoarigato;

public class Image {
	private long id;
	private String url;
	public long getId() {
		return id;
	}
	public String getUrl() {
		return url;
	}
	public Image(long id, String url) {
		super();
		this.id = id;
		this.url = url;
	}
	
}
