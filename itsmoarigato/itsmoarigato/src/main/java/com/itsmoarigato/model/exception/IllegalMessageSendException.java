package com.itsmoarigato.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalMessageSendException extends RuntimeException{
	private static final long serialVersionUID = -7672627473829999396L;

	public IllegalMessageSendException() {
		super();
	}

	public IllegalMessageSendException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalMessageSendException(String message) {
		super(message);
	}

	public IllegalMessageSendException(Throwable cause) {
		super(cause);
	}
}
