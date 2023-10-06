package com.OAuth2.oauth2Login.controller.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/normal")
    @ResponseStatus(HttpStatus.OK)
    public String normal(){
        return "normal";
    }

    @GetMapping("/vip")
    @ResponseStatus(HttpStatus.OK)
    public String vip(){
        return "vip";
    }

}