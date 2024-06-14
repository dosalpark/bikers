package org.example.bikers.global.exception.customException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.bikers.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
