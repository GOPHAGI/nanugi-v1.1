package com.gophagi.nanugi.member.exception;

import com.gophagi.nanugi.common.excepion.CommonExcepion;
import com.gophagi.nanugi.common.excepion.ErrorCode;

public class NotFoundCodeException extends CommonExcepion {

    public NotFoundCodeException(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
