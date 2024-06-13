package org.example.bikers.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.bikers.global.dto.CommonResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "exceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException ex,
        HttpServletRequest request) {
        log.error("url: {}, msg: {}, \n Stacktrace", request.getRequestURI(), ex.getMessage(),
            ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(CommonResponseDto.fail("400", ex.getMessage()));
    }


}
