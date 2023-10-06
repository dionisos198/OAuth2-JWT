package com.OAuth2.oauth2Login.controller.auth;

import com.OAuth2.oauth2Login.dto.SignInDto;
import com.OAuth2.oauth2Login.dto.SignUpDto;
import com.OAuth2.oauth2Login.dto.TokenRequestDto;
import com.OAuth2.oauth2Login.dto.TokenResponseDto;
import com.OAuth2.oauth2Login.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/save/normal")
    @ResponseStatus(HttpStatus.CREATED)
    public void memberSignUp(@RequestBody SignUpDto signUpDto){
        authService.normalMemberSignUp(signUpDto);
    }

    @PostMapping("/save/vip")
    @ResponseStatus(HttpStatus.CREATED)
    public void nonMemberSignUp(@RequestBody SignUpDto signUpDto){
        authService.VIPMemberSignUp(signUpDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TokenResponseDto login(@RequestBody SignInDto signInDto){
        return authService.MemberSingIn(signInDto);
    }

    @PostMapping("/reIssue")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto reIssue(@RequestBody TokenRequestDto tokenRequestDto){
        return authService.reIssue(tokenRequestDto);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestBody TokenRequestDto tokenRequestDto){
        authService.logout(tokenRequestDto);
    }
}