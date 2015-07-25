package com.itsmoarigato;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Image {
	private final long id;
	private final String url;
	@JsonIgnore
	private final byte[] contents;
	public Image(long id, String url, byte[] contents) {
		super();
		this.id = id;
		this.url = url;
		this.contents = contents;
	}
	public long getId() {
		return id;
	}
	public String getUrl() {
		return url;
	}
	public byte[] getContents() {
		return contents;
	}
}
