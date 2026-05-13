package com.example.shop_app.exception;

public class Response {

    private final String code;
    private final String message;

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Response from(ErrorCode errorCode) {
        return new Response(errorCode.getCode(), errorCode.getMessage());
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
