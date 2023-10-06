package com.OAuth2.oauth2Login.dto;


import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Getter
public class SignInDto {
    private String userID;
    private String password;
    public UsernamePasswordAuthenticationToken getAuthenticationToken(){
        return new UsernamePasswordAuthenticationToken(userID,password);
    }
}
