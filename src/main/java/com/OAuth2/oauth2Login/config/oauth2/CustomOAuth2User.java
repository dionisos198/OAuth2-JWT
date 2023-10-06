package com.OAuth2.oauth2Login.config.oauth2;

import com.OAuth2.oauth2Login.domain.Authority;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private String userID;
    private Authority authority;


    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String userID, Authority authority){
        super(authorities,attributes,nameAttributeKey);
        System.out.println("CustomOAuth2User.CustomOAuth2User");
        this.userID=userID;
        this.authority=authority;
        System.out.println("customOAUth2User 생성자");
    }
}
