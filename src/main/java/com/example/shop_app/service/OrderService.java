package com.example.shop_app.service;

import com.example.shop_app.domain.Member;
import com.example.shop_app.domain.Order;
import com.example.shop_app.domain.OrderItem;
import com.example.shop_app.domain.Product;
import com.example.shop_app.dto.OrderItemResponse;
import com.example.shop_app.dto.OrderResponse;
import com.example.shop_app.exception.CustomException;
import com.example.shop_app.exception.ErrorCode;
import com.example.shop_app.exception.ProductNotFoundException;
import com.example.shop_app.repository.OrderItemRepository;
import com.example.shop_app.repository.OrderRepository;
import com.example.shop_app.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(Member member, Long productId, Integer quantity) {
        validateQuantity(quantity);

        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        Integer stockQuantity = product.getStockQuantity();
        if (stockQuantity == null || stockQuantity < quantity) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }

        product.decreaseStock(quantity);

        Order order = orderRepository.save(new Order(member));
        OrderItem orderItem = new OrderItem(order, product, quantity, product.getPrice());
        orderItemRepository.save(orderItem);

        List<OrderItemResponse> items = List.of(
                new OrderItemResponse(product.getId(), product.getName(), quantity, product.getPrice())
        );
        int totalPrice = product.getPrice() * quantity;

        return new OrderResponse(
                order.getId(),
                member.getId(),
                order.getStatus().name(),
                totalPrice,
                items,
                order.getCreatedAt()
        );
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_ORDER_QUANTITY);
        }
    }
}
