package com.james.springbootmall.securityConfig;


import com.james.springbootmall.securityConfig.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers("/products","/users/register", "/users/login", "/users/forgot-password",
                                "/users/verify-code", "/users/reset-password", "/users/login/oauth2/google").permitAll() // "/" 和 "/login" 無需認證
                        .anyRequest().authenticated()          // 其他路徑需要認證
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/products")               // 登入成功後跳轉到首頁
                )
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class); // 添加 JWT 過濾器

        return http.build();
    }
}
