package com.OAuth2.oauth2Login.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@Entity
@AllArgsConstructor
public class VIPMember extends Member{
    public VIPMember (String name,String phone,String userID,String password){
        this.name=name;
        this.phone=phone;
        this.userID=userID;
        this.password=password;
        this.authority=Authority.ROLE_VIP;

    }
}