package com.example.shop_app.controller;

import com.example.shop_app.domain.Member;
import com.example.shop_app.dto.OrderCreateRequest;
import com.example.shop_app.dto.OrderResponse;
import com.example.shop_app.service.MemberService;
import com.example.shop_app.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;

    public OrderController(OrderService orderService, MemberService memberService) {
        this.orderService = orderService;
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(HttpServletRequest httpServletRequest,
                                     @RequestBody OrderCreateRequest request) {
        Member loginMember = memberService.getLoginMember(httpServletRequest);
        return orderService.createOrder(loginMember, request.getProductId(), request.getQuantity());
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(HttpServletRequest httpServletRequest,
                                  @PathVariable Long orderId) {
        Member loginMember = memberService.getLoginMember(httpServletRequest);
        return orderService.getOrder(loginMember, orderId);
    }

    @GetMapping("/me")
    public List<OrderResponse> getMyOrders(HttpServletRequest httpServletRequest) {
        Member loginMember = memberService.getLoginMember(httpServletRequest);
        return orderService.getMyOrders(loginMember);
    }
}
