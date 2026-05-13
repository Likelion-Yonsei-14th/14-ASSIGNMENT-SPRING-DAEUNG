package com.example.shop_app.dto;

import com.example.shop_app.domain.Product;

public class ProductResponse {

    private final Long productId;
    private final Long sellerId;
    private final String sellerNickname;
    private final String name;
    private final String description;
    private final int price;

    public ProductResponse(Long productId, Long sellerId, String sellerNickname, String name,
                           String description, int price) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.sellerNickname = sellerNickname;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getMember().getId(),
                product.getMember().getNickname(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getSellerNickname() {
        return sellerNickname;
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
}
