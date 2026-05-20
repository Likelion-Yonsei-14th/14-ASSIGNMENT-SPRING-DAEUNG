package com.example.shop_app.service;

import com.example.shop_app.auth.AuthConstants;
import com.example.shop_app.auth.JwtProvider;
import com.example.shop_app.domain.Member;
import com.example.shop_app.dto.LoginRequest;
import com.example.shop_app.dto.LoginResponse;
import com.example.shop_app.dto.MemberResponse;
import com.example.shop_app.dto.SignupRequest;
import com.example.shop_app.exception.CustomException;
import com.example.shop_app.exception.ErrorCode;
import com.example.shop_app.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    // 회원가입, 비밀번호 해시로 저장, 이메일 중복 체크
    public MemberResponse signup(SignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = new Member(request.getEmail(), encodedPassword, request.getNickname());
        Member saved = memberRepository.save(member);
        return MemberResponse.from(saved);
    }

    // 세션 로그인
    public MemberResponse loginWithSession(LoginRequest request, HttpSession session) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
        session.setAttribute(AuthConstants.LOGIN_MEMBER_ID, member.getId());
        return MemberResponse.from(member);
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    // JWT 로그인
    public LoginResponse loginWithJwt(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
        String token = jwtProvider.createToken(member.getId());
        return new LoginResponse(token, "Bearer", member.getId(), member.getNickname());
    }
}
