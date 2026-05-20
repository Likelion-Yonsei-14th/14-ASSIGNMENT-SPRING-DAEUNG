package com.example.shop_app.controller;

import com.example.shop_app.dto.LoginRequest;
import com.example.shop_app.dto.LoginResponse;
import com.example.shop_app.dto.MemberResponse;
import com.example.shop_app.dto.MessageResponse;
import com.example.shop_app.dto.SignupRequest;
import com.example.shop_app.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public MemberResponse signup(@RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/session/login")
    public MemberResponse loginWithSession(@RequestBody LoginRequest request, HttpSession session) {
        return authService.loginWithSession(request, session);
    }

    @PostMapping("/session/logout")
    public MessageResponse logout(HttpSession session) {
        authService.logout(session);
        return new MessageResponse("로그아웃되었습니다.");
    }

    @PostMapping("/jwt/login")
    public LoginResponse loginWithJwt(@RequestBody LoginRequest request) {
        return authService.loginWithJwt(request);
    }
}
