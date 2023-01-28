package com.gophagi.nanugi.groupbuying.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class InvalidParticipantInstanceException extends RuntimeException {
	private ErrorCode errorCode;
	public InvalidParticipantInstanceException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
