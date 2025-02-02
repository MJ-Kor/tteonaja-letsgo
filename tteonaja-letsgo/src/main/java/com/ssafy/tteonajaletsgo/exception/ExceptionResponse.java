package com.ssafy.tteonajaletsgo.exception;

import io.swagger.models.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

public class ExceptionResponse {

    @Getter
    private static final ExceptionResponse instance = new ExceptionResponse();

    private ExceptionResponse() {}

    public static ResponseEntity<String> response(Exception e) {
        if (e instanceof SQLException) {
            return new ResponseEntity<String>("sql error", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (e instanceof NotFoundException) {
            return new ResponseEntity<String>("not found exception", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<String>("internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
