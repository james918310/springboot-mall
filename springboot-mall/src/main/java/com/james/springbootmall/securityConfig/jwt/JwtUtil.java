package com.james.springbootmall.securityConfig.jwt;

import com.james.springbootmall.model.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {
    private final String SECRET_KEY = "881216918310jwt94516115" +
            "5454854584841515585" +
            "551151549415484154548"; // 替換為更安全的密鑰
    private final long EXPIRATION_TIME = 1000 * 60 * 60 ; // 1 小時有效期

    // 生成 JWT
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserId().toString()) // 設置主題（用戶名）
                .claim("email",user.getEmail())
                .setIssuedAt(new Date()) // 簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 過期時間
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 使用密鑰簽名
                .compact();
    }

    // 驗證 JWT
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token 過期");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token 格式無效");
        } catch (SignatureException e) {
            throw new RuntimeException("Token 簽名無效");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Token 是空的");
        }
    }

    // 從 Token 中提取用戶名
    public String extractUsername(String token) {
        return validateToken(token).getSubject();
    }
}
