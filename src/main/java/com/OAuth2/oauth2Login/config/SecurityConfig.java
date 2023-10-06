package com.OAuth2.oauth2Login.config;


import com.OAuth2.oauth2Login.config.jwt.JwtAccessDeniedHandler;
import com.OAuth2.oauth2Login.config.jwt.JwtAuthenticationEntryPointHandler;
import com.OAuth2.oauth2Login.config.jwt.JwtSecurityConfig;
import com.OAuth2.oauth2Login.config.jwt.TokenProvider;
import com.OAuth2.oauth2Login.config.oauth2.handler.OAuth2LoginFailureHandler;
import com.OAuth2.oauth2Login.config.oauth2.handler.OAuth2LoginSuccessHandler;
import com.OAuth2.oauth2Login.config.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationEntryPointHandler authenticationEntryPointHandler;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String,String> redisTemplate;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.formLogin().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/normal").access("hasRole('VIP') or hasRole('NORMAL')")
                .antMatchers("/vip").hasRole("VIP")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfig(tokenProvider,redisTemplate))
                .and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .and().build();


    }

}