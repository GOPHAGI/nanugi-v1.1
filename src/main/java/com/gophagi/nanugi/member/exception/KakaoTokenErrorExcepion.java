package com.gophagi.nanugi.member.exception;

import com.gophagi.nanugi.common.excepion.ErrorCode;
import lombok.Getter;

@Getter
public class KakaoTokenErrorExcepion extends RuntimeException{
    private ErrorCode errorCode;

    public KakaoTokenErrorExcepion(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

}
