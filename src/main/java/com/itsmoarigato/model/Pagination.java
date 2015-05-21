package com.itsmoarigato.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pagination {
	private final int limit;
	private final int offset;
	private final Timestamp atPoint;
	
	public Pagination(int limit, int offset, Timestamp atPoint) {
		super();
		this.limit = limit;
		this.offset = offset;
		this.atPoint = atPoint;
	}
	
	public Pagination() {
		this(10,0,null);
	}
	
	public Pagination(int offset) {
		this(10,offset,null);
	}
	
	public Pagination(int offset,Timestamp atPoint) {
		this(10,offset,atPoint);
	}

	public Pagination(Timestamp atPoint) {
		this(10,0,atPoint);
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public Timestamp getAtPoint() {
//		SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
//		return f.format(atPoint);
		return atPoint;
	}

	public boolean hasAtPoint() {
		return atPoint != null;
	}
}
