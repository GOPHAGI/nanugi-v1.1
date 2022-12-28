package com.gophagi.nanugi.common.excepion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //Common
    INVALID_INPUT_VALUE(400, "COMMON-ERR-400", " Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "COMMON-ERR-403", "Access is Denied"),
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    METHOD_NOT_ALLOWED(405, "COMMON-ERR-405", " Invalid Input Value"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),

    //Member
    MEMBER_DUPLICATION(400,"MEMBER-ERR-400","MEMBER DUPLICATED");


    private int status;
    private String errorCode;
    private String message;


}
