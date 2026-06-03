package com.example.shop_app.dto;

public class OrderItemResponse {

    private final Long productId;
    private final String productName;
    private final Integer quantity;
    private final Integer orderPrice;

    public OrderItemResponse(Long productId, String productName, Integer quantity, Integer orderPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getOrderPrice() {
        return orderPrice;
    }
}
