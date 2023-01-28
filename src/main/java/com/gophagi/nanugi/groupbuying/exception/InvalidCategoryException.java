package com.gophagi.nanugi.groupbuying.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class InvalidCategoryException extends RuntimeException {
	private ErrorCode errorCode;
	public InvalidCategoryException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
