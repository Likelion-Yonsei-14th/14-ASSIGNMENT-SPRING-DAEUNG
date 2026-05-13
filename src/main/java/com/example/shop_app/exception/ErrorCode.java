package com.example.shop_app.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_NOT_FOUND", "상품을 찾을 수 없음"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "등록자를 찾을 수 없음"),
    INVALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_NAME", "상품명이 비어 있음"),
    INVALID_PRODUCT_DESCRIPTION(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_DESCRIPTION", "상품 설명이 비어 있음"),
    INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_PRICE", "상품 가격이 0 이하"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 오류");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
