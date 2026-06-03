package com.example.shop_app.service;

import com.example.shop_app.domain.Member;
import com.example.shop_app.domain.Order;
import com.example.shop_app.domain.OrderItem;
import com.example.shop_app.domain.Product;
import com.example.shop_app.dto.OrderItemResponse;
import com.example.shop_app.dto.OrderResponse;
import com.example.shop_app.exception.CustomException;
import com.example.shop_app.exception.ErrorCode;
import com.example.shop_app.exception.OrderNotFoundException;
import com.example.shop_app.exception.ProductNotFoundException;
import com.example.shop_app.repository.OrderItemRepository;
import com.example.shop_app.repository.OrderRepository;
import com.example.shop_app.repository.ProductRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (!order.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        List<OrderItem> items = orderItemRepository.findAllByOrderId(orderId);
        return toOrderResponse(order, items);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Member member) {
        List<Order> orders = orderRepository.findAllByMemberIdOrderByIdDesc(member.getId());
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .toList();
        Map<Long, List<OrderItem>> itemsByOrderId = orderItemRepository.findAllByOrderIdIn(orderIds)
                .stream()
                .collect(Collectors.groupingBy(item -> item.getOrder().getId(), HashMap::new, Collectors.toList()));

        return orders.stream()
                .map(order -> toOrderResponseWithoutItems(order, itemsByOrderId.get(order.getId())))
                .toList();
    }

        @Transactional
        public OrderResponse cancelOrder(Member member, Long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(OrderNotFoundException::new);
                if (!order.getMember().getId().equals(member.getId())) {
                        throw new CustomException(ErrorCode.FORBIDDEN);
                }
                if (order.getStatus() == Order.OrderStatus.CANCELED) {
                        throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELED);
                }

                List<OrderItem> items = orderItemRepository.findAllByOrderId(orderId);
                for (OrderItem item : items) {
                        item.getProduct().increaseStock(item.getQuantity());
                }

                order.cancel();
                return toOrderResponse(order, items);
        }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_ORDER_QUANTITY);
        }
    }

    private OrderResponse toOrderResponse(Order order, List<OrderItem> orderItems) {
        List<OrderItemResponse> items = orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getOrderPrice()))
                .toList();
        int totalPrice = orderItems.stream()
                .mapToInt(item -> item.getOrderPrice() * item.getQuantity())
                .sum();

        return new OrderResponse(
                order.getId(),
                order.getMember().getId(),
                order.getStatus().name(),
                totalPrice,
                items,
                order.getCreatedAt()
        );
    }

    private OrderResponse toOrderResponseWithoutItems(Order order, List<OrderItem> orderItems) {
        // 모든 주문을 조회할 때 쉽게 보기 위해서 items는 포함x
        int totalPrice = 0;
        if (orderItems != null) {
            totalPrice = orderItems.stream()
                    .mapToInt(item -> item.getOrderPrice() * item.getQuantity())
                    .sum();
        }

        return new OrderResponse(
                order.getId(),
                order.getMember().getId(),
                order.getStatus().name(),
                totalPrice,
                null, // JSON에서 제외됨
                order.getCreatedAt()
        );
    }
}
