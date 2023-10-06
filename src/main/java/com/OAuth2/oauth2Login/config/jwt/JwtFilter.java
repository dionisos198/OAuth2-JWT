package com.OAuth2.oauth2Login.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final String AUTHORIZATION_KEY = "Authorization";
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String,String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = parseHeader(request);

        if(StringUtils.hasText(tokenValue) && tokenProvider.validateToken(tokenValue)) {
            String logOut=(String) redisTemplate.opsForValue().get(tokenValue);
            if(ObjectUtils.isEmpty(logOut)){
                Authentication authentication = tokenProvider.getAuthentication(tokenValue);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    public String parseHeader(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_KEY);

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}