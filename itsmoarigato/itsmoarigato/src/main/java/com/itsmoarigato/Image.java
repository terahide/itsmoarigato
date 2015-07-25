package com.itsmoarigato;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Image {
	private final long id;
	@JsonIgnore
	private final byte[] contents;
	public Image(long id, byte[] contents) {
		super();
		this.id = id;
		this.contents = contents;
	}
	public long getId() {
		return id;
	}
	public byte[] getContents() {
		return contents;
	}
}
