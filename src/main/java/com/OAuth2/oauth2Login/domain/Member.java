package com.OAuth2.oauth2Login.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class  Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;


    @Column(name="name",nullable = false)
    protected String name;

    @Column(name = "phone",nullable = false)
    protected String phone;

    @Column(name = "userID",nullable = false)
    protected String userID;

    @Column(name = "password",nullable = false)
    protected String password;

    @Enumerated(value = EnumType.STRING)
    protected Authority authority;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "socialId")
    protected String socialId;//로그인 한 소셜 타입의 식별자 값 (일반 로그인인 경우) NULL

     @Column(name = "imageUrl")
    protected String imageUrl;


     @Column(name = "nickName")
     protected String nickname;


}