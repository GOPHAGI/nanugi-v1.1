package com.gophagi.nanugi.groupbuying.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class PersonnelLimitExceededException extends RuntimeException {
	private ErrorCode errorCode;
	public PersonnelLimitExceededException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
