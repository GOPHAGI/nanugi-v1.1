package com.gophagi.nanugi.groupbuying.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class DuplicateParticipationException extends RuntimeException {
	private ErrorCode errorCode;
	public DuplicateParticipationException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
