package com.example.shop_app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    protected Product() {
    }

    public Product(Member member, String name, String description, int price, Integer stockQuantity) {
        this.member = member;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void update(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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

    public Integer getStockQuantity() {
        return stockQuantity;
    }
}
