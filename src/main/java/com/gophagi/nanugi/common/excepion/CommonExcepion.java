package com.gophagi.nanugi.common.excepion;

import lombok.Getter;

@Getter
public class CommonExcepion extends RuntimeException{
    private final ErrorCode errorCode;

    public CommonExcepion(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }
}
