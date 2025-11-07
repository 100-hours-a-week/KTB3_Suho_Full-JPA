package kr.adapterz.jpa_practice.dto.common;

import lombok.Getter;

@Getter
public class CommonResponse<T> {
    private String code;
    private String message;
    private T data;

    public CommonResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}