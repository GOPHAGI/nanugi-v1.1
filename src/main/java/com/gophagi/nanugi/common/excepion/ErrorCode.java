package com.gophagi.nanugi.common.excepion;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	//Common
	INVALID_INPUT_VALUE(400, "400 Bad Request", "잘못된 문법으로 인하여 서버가 요청하여 이해할 수 없습니다."),
	HANDLE_ACCESS_DENIED(403, "403 Forbidden", "해당 컨텐츠에 접근할 권한이 없습니다."),
	NOT_FOUND(404, "404 Not Found", "PAGE NOT FOUND"),
	METHOD_NOT_ALLOWED(405, "405 Method Not Allowed", "허용하지 않는 요청 Method 입니다."),
	INTER_SERVER_ERROR(500, "500 Internal Server Error", "INTER SERVER ERROR"),
	FAIL_UPLOAD(500, "500 Fail Upload", "FAIL UPLOAD"),

    //Member
    MEMBER_DUPLICATION(400,"MEMBER-ERR-400","MEMBER DUPLICATED"),
    CODE_NOT_FOUND(400, "KAKAO-ERR-400", "authorization code not found"),
    INVALID_TOKEN(401, "KAKAO-ERR-401", "this access token does not exist"),


	//Groupbuying
	CANNOT_DELETE_BOARD(500, "500 CANNOT DELETE BOARD", "공동구매 게시글을 삭제할 수 없습니다."),
	PARTICIPATION_DUPLICATION(500, "500 PARTICIPATION_DUPLICATION", "중복된 참여 신청입니다."),
	PERSONNEL_LIMIT_EXCEEDED(500, "500 PERSONNEL_LIMIT_EXCEEDED", "제한 인원 초과하였습니다."),
	INVALID_PARTICIPANT_INSTANCE(500, "500 INVALID_PARTICIPANT_INSTANCE", "참여자 인스턴스를 생성할 수 없습니다."),
	INVALID_BOARD_INSTANCE(500, "500 INVALID_BOARD_INSTANCE", "공동구매 보드 인스턴스를 생성할 수 없습니다.")
	;

    private int status;
    private String errorCode;
    private String message;

}
