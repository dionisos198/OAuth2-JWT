package com.OAuth2.oauth2Login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpirationTime;
}