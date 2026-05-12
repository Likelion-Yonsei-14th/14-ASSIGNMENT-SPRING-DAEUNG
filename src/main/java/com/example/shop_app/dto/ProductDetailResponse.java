package com.example.shop_app.dto;

import com.example.shop_app.domain.Product;
import java.time.LocalDateTime;

public class ProductDetailResponse {

    private final Long productId;
    private final Long memberId;
    private final String memberNickname;
    private final String name;
    private final String description;
    private final int price;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ProductDetailResponse(Long productId, Long memberId, String memberNickname, String name,
                                 String description, int price, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getMember().getId(),
                product.getMember().getNickname(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
