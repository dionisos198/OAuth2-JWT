package com.OAuth2.oauth2Login.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
@Getter
public class TokenRequestDto {
    @NotBlank(message = "토큰 재발급을 위하여 accessToken의 값을 입력해주세요.")
    private String accessToken;

    @NotBlank(message = "토큰 재발급을 위하여 refreshToken의 값을 입력해주세요.")
    private String refreshToken;
}