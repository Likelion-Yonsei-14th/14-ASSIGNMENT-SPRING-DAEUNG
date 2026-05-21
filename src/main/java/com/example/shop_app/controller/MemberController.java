package com.example.shop_app.controller;

import com.example.shop_app.dto.MemberResponse;
import com.example.shop_app.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public MemberResponse getMe(HttpServletRequest request) {
        return memberService.getCurrentMember(request);
    }
}
