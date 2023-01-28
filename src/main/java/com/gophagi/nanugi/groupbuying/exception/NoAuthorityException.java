package com.gophagi.nanugi.groupbuying.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class NoAuthorityException extends RuntimeException {
	private ErrorCode errorCode;

	public NoAuthorityException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}
