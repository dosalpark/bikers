package org.example.bikers.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseDto<T> {

    private String status;
    private String msg;
    private T data;

    public CommonResponseDto(T data) {
        this.data = data;
    }

    public CommonResponseDto(String status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonResponseDto<T> success(T data) {
        return new CommonResponseDto<>(data);
    }

    public static <T> CommonResponseDto<T> success(String status, String msg, T data) {
        return new CommonResponseDto<>(status, msg, data);
    }

    public static CommonResponseDto fail(String status, String msg) {
        return new CommonResponseDto(status, msg, null);
    }

}
