package org.example.bikers.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NO_MATCHING_MANUFACTURER("404", "BM000001", "일치하는 제조사가 없습니다."),
    NO_MATCHING_CATEGORY("404", "BM000002", "일치하는 카테고리가 없습니다."),
    MODEL_ALREADY_REGISTERED("409", "BM000003", "이미 등록된 모델입니다."),
    NO_SUCH_BIKE_MODEL("404", "BM000004", "해당하는 바이크모델이 없습니다."),
    NO_BIKE_MODEL_FOUND("404", "BM000005", "조회 할 바이크모델이 없습니다.");

    private final String httpStatus;
    private final String code;
    private final String msg;

    public static boolean isCode(String code) {
        ErrorCode[] errorCodes = ErrorCode.values();
        for (ErrorCode errorCode : errorCodes) {
            if (errorCode.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static ErrorCode getValue(String code) {
        ErrorCode[] errorCodes = ErrorCode.values();
        for (ErrorCode errorCode : errorCodes) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        throw new IllegalArgumentException("에러코드를 찾을 수 없습니다.");
    }
}