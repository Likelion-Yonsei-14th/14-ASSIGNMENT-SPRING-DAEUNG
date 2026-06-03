package com.example.shop_app.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private final Long orderId;
    private final Long memberId;
    private final String status;
    private final Integer totalPrice;
    private final List<OrderItemResponse> items;
    private final LocalDateTime createdAt;

    public OrderResponse(Long orderId, Long memberId, String status, Integer totalPrice,
                         List<OrderItemResponse> items, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.items = items;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getStatus() {
        return status;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
