package com.growith.tailo.common.config;


import com.growith.tailo.auth.jwt.JwtTokenFilter;
import com.growith.tailo.auth.jwt.handler.JwtAccessDeniedHandler;
import com.growith.tailo.auth.jwt.service.JwtAuthenticationEntryPoint;
import com.growith.tailo.auth.oauth.handler.CustomOAuth2AuthenticationFailureHandler;
import com.growith.tailo.auth.oauth.handler.CustomOAuth2AuthenticationSuccessHandler;
import com.growith.tailo.auth.oauth.service.CustomOAuth2OidcUserService;
import com.growith.tailo.auth.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2OidcUserService customOAuth2OidcUserService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOAuth2OidcUserService))
                                .successHandler(customOAuth2AuthenticationSuccessHandler)
                                .failureHandler(customOAuth2AuthenticationFailureHandler)
//                        .defaultSuccessUrl("/home", true)
//                        .failureUrl("/login?error=true")
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
