package com.example.shop_app.controller;

import com.example.shop_app.domain.Product;
import com.example.shop_app.dto.ProductCreateRequest;
import com.example.shop_app.dto.ProductDetailResponse;
import com.example.shop_app.dto.ProductSummaryResponse;
import com.example.shop_app.service.ProductService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetailResponse createProduct(@RequestBody ProductCreateRequest request) {
        Product product = productService.createProduct(
                request.getMemberId(),
                request.getName(),
                request.getDescription(),
                request.getPrice()
        );

        return ProductDetailResponse.from(product);
    }

    @GetMapping
    public List<ProductSummaryResponse> getProducts() {
        return productService.getProducts().stream()
                .map(ProductSummaryResponse::from)
                .toList();
    }

    @GetMapping("/{productId}")
    public ProductDetailResponse getProduct(@PathVariable Long productId) {
        Product product = productService.getProduct(productId);
        return ProductDetailResponse.from(product);
    }
}
