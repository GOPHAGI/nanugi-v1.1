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
    MEMBER_DUPLICATION(400,"MEMBER-ERR-400","MEMBER DUPLICATED"),
    CODE_NOT_FOUND(400, "KAKAO-ERR-400", "authorization code not found"),
    INVALID_TOKEN(401, "KAKAO-ERR-401", "this access token does not exist"),

    //File
    FAIL_UPLOAD(500,"FILE-ERR-500","파일 업로드를 실패하였습니다.")
    ;


    private int status;
    private String errorCode;
    private String message;


}
