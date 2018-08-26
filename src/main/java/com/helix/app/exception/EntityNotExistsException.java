package com.helix.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityNotExistsException extends RuntimeException {

	public EntityNotExistsException(String msg) {
		super(msg);
	}
}
