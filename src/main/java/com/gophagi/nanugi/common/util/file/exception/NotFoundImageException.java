package com.gophagi.nanugi.common.util.file.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class NotFoundImageException extends RuntimeException {
	private ErrorCode errorCode;
	public NotFoundImageException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
