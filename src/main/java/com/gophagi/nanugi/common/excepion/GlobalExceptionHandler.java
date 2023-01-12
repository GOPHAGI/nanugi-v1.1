package com.gophagi.nanugi.common.excepion;


import com.gophagi.nanugi.member.exception.DuplicateMemberExcepion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonExcepion.class)
    public ResponseEntity<ErrorResponse> CommonExcepionHandler(CommonExcepion ex){
        log.error("CommonExcepion",ex);
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindExceptionHandler(BindException ex){
        log.error("bindExceptionHandler",ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateMemberExcepion.class)
    public ResponseEntity<ErrorResponse> runtimeExceptionHandler(DuplicateMemberExcepion ex){
        log.error("runtimeExceptionHandler",ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.MEMBER_DUPLICATION);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("handleException",ex);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTER_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
