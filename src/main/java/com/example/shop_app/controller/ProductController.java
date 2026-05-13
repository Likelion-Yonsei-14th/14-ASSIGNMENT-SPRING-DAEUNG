package com.example.shop_app.controller;

import com.example.shop_app.domain.Product;
import com.example.shop_app.dto.ProductCreateRequest;
import com.example.shop_app.dto.ProductResponse;
import com.example.shop_app.dto.ProductUpdateRequest;
import com.example.shop_app.service.ProductService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ProductResponse createProduct(@RequestBody ProductCreateRequest request) {
        Product product = productService.createProduct(
                request.getSellerId(),
                request.getName(),
                request.getDescription(),
                request.getPrice()
        );

        return ProductResponse.from(product);
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable Long productId) {
        Product product = productService.getProduct(productId);
        return ProductResponse.from(product);
    }

    @PatchMapping("/{productId}")
    public ProductResponse updateProduct(@PathVariable Long productId,
                                         @RequestBody ProductUpdateRequest request) {
        Product product = productService.updateProduct(
                productId,
                request.getName(),
                request.getDescription(),
                request.getPrice()
        );
        return ProductResponse.from(product);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword).stream()
                .map(ProductResponse::from)
                .toList();
    }
}
