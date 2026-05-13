package com.example.shop_app.service;

import com.example.shop_app.domain.Member;
import com.example.shop_app.domain.Product;
import com.example.shop_app.exception.CustomException;
import com.example.shop_app.exception.ErrorCode;
import com.example.shop_app.exception.MemberNotFoundException;
import com.example.shop_app.exception.ProductNotFoundException;
import com.example.shop_app.repository.MemberRepository;
import com.example.shop_app.repository.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public ProductService(MemberRepository memberRepository, ProductRepository productRepository) {
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Product createProduct(Long sellerId, String name, String description, int price) {
        validateProductFields(name, description, price);

        Member member = memberRepository.findById(sellerId)
                .orElseThrow(MemberNotFoundException::new);

        Product product = new Product(member, name, description, price);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    public Product updateProduct(Long productId, String name, String description, int price) {
        validateProductFields(name, description, price);

        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        product.update(name, description, price);
        return product;
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_NAME);
        }
        return productRepository.findAllByNameContaining(keyword);
    }

    private void validateProductFields(String name, String description, int price) {
        if (name == null || name.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_NAME);
        }
        if (description == null || description.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_DESCRIPTION);
        }
        if (price <= 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_PRICE);
        }
    }
}
