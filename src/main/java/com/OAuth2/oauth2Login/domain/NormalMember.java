package com.OAuth2.oauth2Login.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;

@Getter
@Entity
@AllArgsConstructor
public class NormalMember extends Member {
    public NormalMember (String name,String phone,String userID,String password){
        this.name=name;
        this.phone=phone;
        this.userID=userID;
        this.password=password;
        this.authority=Authority.ROLE_NORMAL;
        this.imageUrl=null;
        this.socialId=null;
        this.nickname=null;
    }
    public NormalMember (String name,String phone,String userID,String password,String imageUrl,String socialId,String nickname){
        this.name=name;
        this.phone=phone;
        this.userID=userID;
        this.password=password;
        this.authority=Authority.ROLE_NORMAL;
        this.imageUrl=imageUrl;
        this.socialId=socialId;
        this.nickname=nickname;
    }

}