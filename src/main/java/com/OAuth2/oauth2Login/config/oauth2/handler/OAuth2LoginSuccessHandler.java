package com.OAuth2.oauth2Login.config.oauth2.handler;


import com.OAuth2.oauth2Login.config.jwt.TokenProvider;
import com.OAuth2.oauth2Login.config.oauth2.CustomOAuth2User;
import com.OAuth2.oauth2Login.domain.Authority;
import com.OAuth2.oauth2Login.dto.TokenDto;
import com.OAuth2.oauth2Login.dto.TokenResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 성공!");
        //accessToken 과 refreshToken을 반환한다. response body 에 담을 수도 header 에 담을 수도 있고 redirect 를 시켜 줄 수 도 있다.
        try{
             /*  :: 헤더에 accessToken 및 refreshToken을 넣고 Redirect 시킬 수도 있다.
               String accessToken=jwtService.createAccessToken(oAuth2User.getEmail());
               response.addHeader(jwtService.getAccessHeader(),"Bearer "+accessToken);
                response.sendRedirect("/login");
               jwtService.sendAccessAndRefreshToken(response,accessToken,null);*/
               /* String accessToken = jwtService.createAccessToken(oAuth2User.getEmail()); // accessToken을 얻는 코드
                String refreshToken = jwtService.createRefreshToken(); // refreshToken을 얻는 코드*/

            //
            System.out.println("authentication.getName()"+authentication.getName());//{id=BW2G_z9w9scd..., profile_image=https://phinf.pstatic.net/contact/20230615_207/1686815329264fVtbk_PNG/%BD%BA%C5%A9%B8%B0%BC%A6_2023-06-15_164754.png, email=dionisos198@naver.com, name=이진우}
            System.out.println("authentication.getAuthorities()"+authentication.getAuthorities());//[ROLE_NORMAL]
            System.out.println("authentication.getPrincipal().getUserID()"+((CustomOAuth2User) authentication.getPrincipal()).getUserID());//BW2G_z9w9scdOsV...

            CustomOAuth2User oAuth2User=((CustomOAuth2User) authentication.getPrincipal());
            Map<String,Object> attributes=oAuth2User.getAttribute("response");
            TokenDto tokenDto=tokenProvider.createTokenByOAuth(oAuth2User);//OAuth2로 새로운 access,refreshToken 생성
            // TokenResponseDto 객체 생성
            TokenResponseDto tokenResponseDto = new TokenResponseDto(tokenDto.getType(),tokenDto.getAccessToken(),tokenDto.getRefreshToken(),tokenDto.getAccessTokenValidationTime());

                System.out.println("oauth2User.getUserID().toString()"+oAuth2User.getUserID().toString());//BW2G_z9...
                System.out.println("oauth2User.toString()"+oAuth2User.toString());//Name: [{id=BW2G_z9w9scdOsVufVwhjMJ..., profile_image=https://phinf.pstatic.net/contact/20230615_207/1686815329264fVtbk_PNG/%BD%BA%C5%A9%B8%B0%BC%A6_2023-06-15_164754.png, email=dionisos198@naver.com, name=이진우}], Granted Authorities: [[ROLE_NORMAL]], User Attributes: [{resultcode=00, message=success, response={id=BW2G_z9w9scdOsVufVwhjMJw-LCAKNMqVSTZ8he6XN0, profile_image=https://phinf.pstatic.net/contact/20230615_207/1686815329264fVtbk_PNG/%BD%BA%C5%A9%B8%B0%BC%A6_2023-06-15_164754.png, email=dionisos198@naver.com, name=이진우}}]
                System.out.println("oauth2User.getAutority().toString()"+oAuth2User.getAuthority().toString());//ROLE_NORMAL
                System.out.println("attributes.get(id)"+attributes.get("id"));//BW2G_z9w9scdOsVufVwhjM...
                System.out.println("attributes.getAttribute(response).toString()"+oAuth2User.getAttribute("response").toString());//{id=BW2G_z9w9scdOsVufVwhjMJw-LCAKNMqVS..
                System.out.println("response.getWriter().toString()"+response.getWriter().toString());

                redisTemplate.opsForValue().set(attributes.get("id").toString(),tokenDto.getRefreshToken(),tokenDto.getRefreshTokenValidationTime(), TimeUnit.MILLISECONDS);
                // Dto 객체를 JSON으로 변환하여 응답으로 전송
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getWriter(), tokenResponseDto);
        }catch (Exception e){
            throw e;
        }
    }


}
