package com.gophagi.nanugi.member.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class InvalidMemberInstanceException extends RuntimeException {
	private ErrorCode errorCode;
	public InvalidMemberInstanceException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
