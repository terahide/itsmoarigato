package com.itsmoarigato.model;

import java.util.Date;

public class Pagination {
	private final int limit;
	private final int offset;
	private final Date atPoint;
	
	public Pagination(int limit, int offset, Date atPoint) {
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
	
	public Pagination(int offset,Date atPoint) {
		this(10,offset,atPoint);
	}

	public Pagination(Date atPoint) {
		this(10,1,atPoint);
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public Date getAtPoint() {
		return atPoint;
	}
}
