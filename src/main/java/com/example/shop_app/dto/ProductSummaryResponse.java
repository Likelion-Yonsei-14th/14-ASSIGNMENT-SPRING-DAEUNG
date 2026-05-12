package com.example.shop_app.dto;

import com.example.shop_app.domain.Product;

public class ProductSummaryResponse {

    private final Long productId;
    private final Long memberId;
    private final String memberNickname;
    private final String name;
    private final int price;

    public ProductSummaryResponse(Long productId, Long memberId, String memberNickname, String name, int price) {
        this.productId = productId;
        this.memberId = memberId;
        this.memberNickname = memberNickname;
        this.name = name;
        this.price = price;
    }

    public static ProductSummaryResponse from(Product product) {
        return new ProductSummaryResponse(
                product.getId(),
                product.getMember().getId(),
                product.getMember().getNickname(),
                product.getName(),
                product.getPrice()
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

    public int getPrice() {
        return price;
    }
}
