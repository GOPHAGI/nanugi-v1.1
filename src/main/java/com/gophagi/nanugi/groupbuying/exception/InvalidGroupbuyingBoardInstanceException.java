package com.gophagi.nanugi.groupbuying.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;

public class InvalidGroupbuyingBoardInstanceException
	extends RuntimeException {
	private ErrorCode errorCode;
	public InvalidGroupbuyingBoardInstanceException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
