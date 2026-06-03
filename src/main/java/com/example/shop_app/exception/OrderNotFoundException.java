package com.example.shop_app.exception;

public class OrderNotFoundException extends CustomException {

    public OrderNotFoundException() {
        super(ErrorCode.ORDER_NOT_FOUND);
    }
}
