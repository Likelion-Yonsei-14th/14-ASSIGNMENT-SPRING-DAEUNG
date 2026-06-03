package com.example.shop_app.controller;

import com.example.shop_app.domain.Member;
import com.example.shop_app.dto.OrderCreateRequest;
import com.example.shop_app.dto.OrderResponse;
import com.example.shop_app.service.MemberService;
import com.example.shop_app.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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
}
