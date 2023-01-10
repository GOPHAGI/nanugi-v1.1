package com.gophagi.nanugi.member.exception;

import com.gophagi.nanugi.common.excepion.CommonExcepion;
import com.gophagi.nanugi.common.excepion.ErrorCode;
import lombok.Getter;

@Getter
public class InvaildKakaoTokenExcepion extends CommonExcepion {

    public InvaildKakaoTokenExcepion(Throwable cause, ErrorCode errorCode) {
        super(cause, errorCode);
    }
}
