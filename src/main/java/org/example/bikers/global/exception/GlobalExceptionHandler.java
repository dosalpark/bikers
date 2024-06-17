package org.example.bikers.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.exception.customException.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "exceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException ex,
        HttpServletRequest request) {
        if (ErrorCode.isCode(ex.getMessage())) {
            ErrorCode errorCode = ErrorCode.getValue(ex.getMessage());
            log.error("url: {}, errorCode: {}, msg: {}", request.getRequestURI(),
                errorCode.getCode(), errorCode.getMsg());
            return ResponseEntity.status(Integer.parseInt(errorCode.getHttpStatus()))
                .body(CommonResponseDto.fail(errorCode.getHttpStatus(), errorCode.getMsg()));
        }
        log.error("url: {}, msg: {}, \n Stacktrace", request.getRequestURI(), ex.getMessage(),
            ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(CommonResponseDto.fail("400", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
        HttpServletRequest request) {
        List<ObjectError> result = ex.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError objectError : result) {
            sb.append(objectError.getDefaultMessage()).append("\n");
        }
        log.error("url: {}, msg: {}, \n Stacktrace", request.getRequestURI(), ex.getMessage(),
            ex.fillInStackTrace());
        return ResponseEntity.badRequest().body(CommonResponseDto.fail("400", sb.toString()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException ex,
        HttpServletRequest request) {
        log.error("url: {}, errorCode: {}, msg: {}", request.getRequestURI(),
            ex.getErrorCode().getCode(), ex.getErrorCode().getMsg());
        return ResponseEntity.status(Integer.parseInt(ex.getErrorCode().getHttpStatus())).body(
            CommonResponseDto.fail(ex.getErrorCode().getHttpStatus(), ex.getErrorCode().getMsg()));
    }

}
