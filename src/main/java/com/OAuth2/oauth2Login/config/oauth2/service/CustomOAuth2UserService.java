package com.OAuth2.oauth2Login.config.oauth2.service;

import com.OAuth2.oauth2Login.config.oauth2.CustomOAuth2User;
import com.OAuth2.oauth2Login.config.oauth2.OAuthAttributes;
import com.OAuth2.oauth2Login.domain.Member;
import com.OAuth2.oauth2Login.domain.SocialType;
import com.OAuth2.oauth2Login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    private static final String NAVER="naver";
    private static final String KAKAO="kakao";
    //클라이언트 애플리케이션에 인증 코드가 반환이되면
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행-OAuth2 로그인 요청 진입");
        System.out.println("userRequest.getAccessToken():"+userRequest.getAccessToken().toString());//userRequest.getAccessToken():org.springframework.security.oauth2.core.OAuth2AccessToken@51cd5281
        System.out.println("userRequest.getClientRegistration().getClientName():"+userRequest.getClientRegistration().getClientName());//Naver
        System.out.println("userRequest.getClientRegistration().getRegistrationId():"+userRequest.getClientRegistration().getRegistrationId());//naver:인증 공급자 식별
        System.out.println("userRequest.getClientRegistration().getClientId():"+userRequest.getClientRegistration().getClientId());//xbE7Tr8HbUZBYcKMrPFY
        System.out.println("userRequest.getClientRegistration().getProviderDetails():"+userRequest.getClientRegistration().getProviderDetails().toString());//org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@16afb668
        System.out.println("userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint():"+userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());//사용자 정보를 가져올때 필요한 end point,https://openapi.naver.com/v1/nid/me
        System.out.println("userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName():"+userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName().toString());// 사용자의 식별자(attribute)를 추출하기 위한 키(key) 또는 속성(attribute)의 이름을 반환 여기서 response 반환
        System.out.println("userRequest.getClientRegistration().getClientSecret():"+userRequest.getClientRegistration().getClientSecret());//mG0bpG6aXp
        System.out.println("userRequest.getClientRegistration().getRedirectUri():"+userRequest.getClientRegistration().getRedirectUri());//인증 토큰 또는 코드를 수신하기 위한 URL,http://localhost:8080/login/oauth2/code/naver
        System.out.println("userRequest.getClientRegistration().getAuthorizationGrantType():"+userRequest.getClientRegistration().getAuthorizationGrantType().AUTHORIZATION_CODE.getValue());//authorization_code
        System.out.println("userRequest.getClientRegistration().getClientAuthenticationMethod():"+userRequest.getClientRegistration().getClientAuthenticationMethod().getValue());//client_secret_basic
        System.out.println("userRequest.getClientRegistration().getScopes():"+userRequest.getClientRegistration().getScopes());//[name, email, profile_image]
        System.out.println("==================================================");
        /*DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         DefulaOAuth2UserSerivce의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URL로 요청을 보내서
         사용자정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성후 반환한다.
         결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저*/
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate=new DefaultOAuth2UserService();
        OAuth2User oAuth2User=delegate.loadUser(userRequest);

       /* userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
                http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
        userNameAttributeName은 이후에 nameAttrubuteKey로 설정된다.*/
        String registrationId=userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType=getSocialType(registrationId);
        String userNameAttributeName=userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();//OAuth2 로그인 시 PK가 되는 값
        Map<String,Object> attributes=oAuth2User.getAttributes();
        OAuthAttributes extractAttributes=OAuthAttributes.of(socialType,userNameAttributeName,attributes);
        Member createdMember=getMember(extractAttributes,socialType);

        System.out.println("CustomOAuth2UserService.loadUser"+"종료");
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getAuthority().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getUserID(),
                createdMember.getAuthority()
        );

    }

    private SocialType getSocialType(String registrationId){
        System.out.println("CustomOAuth2UserService.getSocialType");
        if(NAVER.equals(registrationId)){
            return SocialType.NAVER;
        }
        if(KAKAO.equals(registrationId)){
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }
    private Member getMember(OAuthAttributes attributes,SocialType socialType){
        System.out.println("CustomOAuth2UserService.getMember");
        Member findMember=memberRepository.findBySocialTypeAndSocialId(socialType,attributes.getOauth2UserInfo().getId()).orElse(null);
        System.out.printf("findMember="+findMember);
        if(findMember==null){
            return saveMember(attributes,socialType);
        }
        return findMember;
    }
    private Member saveMember(OAuthAttributes attributes,SocialType socialType){
        System.out.println("CustomOAuth2UserService.saveMember");
        Member createdMember=attributes.toEntity(socialType,attributes.getOauth2UserInfo());
        return memberRepository.save(createdMember);

    }
}
