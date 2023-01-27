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
	MEMBER_DUPLICATION(400, "MEMBER-ERR-400", "MEMBER DUPLICATED"),
	CODE_NOT_FOUND(400, "KAKAO-ERR-400", "authorization code not found"),
	INVALID_TOKEN(401, "KAKAO-ERR-401", "this access token does not exist"),

	//Groupbuying
	INVALID_ROLE(500, "500 Invalid ROLE", "유효하지 않은 ROLE 입니다."),
	INVALID_CATEGORY(500, "500 Invalid CATEGORY", "유효하지 않은 카테고리 입니다."),
	INVALID_STATUS(500, "500 Invalid STATUS", "유효하지 않은 STATUS 입니다."),
	CANNOT_UPDATE_BOARD(500, "500 Cannot Update Board", "공동구매 게시글을 수정할 수 없습니다."),
	CANNOT_CANCEL_REQUEST(500, "500 Cannot Cancel Board", "공동구매 요청을 취소할 수 없습니다."),
	CANNOT_DELETE_BOARD(500, "500 Cannot Delete Board", "공동구매 게시글을 삭제할 수 없습니다."),
	PARTICIPATION_DUPLICATION(500, "500 Participant Duplication", "중복된 참여 신청입니다."),
	PERSONNEL_LIMIT_EXCEEDED(500, "500 Personnel Limit Exceeded", "제한 인원 초과하였습니다."),
	NOT_FOUND_IMAGE(500, "500 Not Found Image", "이미지 파일을 찾을 수 없습니다."),
	INSERT_ERROR(500, "500 Insert Error", "DB 저장 시 오류가 발생했습니다."),
	RETRIEVE_ERROR(500, "500 Retrieve Error", "DB 조회 시 오류가 발생했습니다."),
	NOT_PROMOTER(500, "500 Not Promoter Error", "권한이 없는 요청입니다."),
	NOT_PARTICIPANT(500, "500 Not Participant Error", "권한이 없는 요청입니다.");

	private int status;
	private String errorCode;
	private String message;

}
