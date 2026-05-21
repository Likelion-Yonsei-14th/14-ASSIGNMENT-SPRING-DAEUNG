package com.example.shop_app.service;

import com.example.shop_app.auth.AuthConstants;
import com.example.shop_app.auth.JwtProvider;
import com.example.shop_app.domain.Member;
import com.example.shop_app.dto.MemberResponse;
import com.example.shop_app.exception.CustomException;
import com.example.shop_app.exception.ErrorCode;
import com.example.shop_app.exception.MemberNotFoundException;
import com.example.shop_app.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional(readOnly = true)
    public MemberResponse getCurrentMember(HttpServletRequest request) {
        Long memberId = resolveMemberId(request);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public Member getLoginMember(HttpServletRequest request) {
        Long memberId = resolveMemberId(request);
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Long resolveMemberId(HttpServletRequest request) {
        Long memberId = getMemberIdFromSession(request);
        if (memberId == null) {
            String authorizationHeader = request.getHeader("Authorization");
            memberId = getMemberIdFromToken(authorizationHeader);
        }
        if (memberId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return memberId;
    }

    private Long getMemberIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object sessionMemberId = session.getAttribute(AuthConstants.LOGIN_MEMBER_ID);
        if (sessionMemberId instanceof Number) {
            return ((Number) sessionMemberId).longValue();
        }
        return null;
    }

    private Long getMemberIdFromToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }
        if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return jwtProvider.getMemberId(token);
    }
}
