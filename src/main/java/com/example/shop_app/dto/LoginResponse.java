package com.example.shop_app.dto;

public class LoginResponse {

    private final String accessToken;
    private final String tokenType;
    private final Long memberId;
    private final String nickname;

    public LoginResponse(String accessToken, String tokenType, Long memberId, String nickname) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.memberId = memberId;
        this.nickname = nickname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getNickname() {
        return nickname;
    }
}
