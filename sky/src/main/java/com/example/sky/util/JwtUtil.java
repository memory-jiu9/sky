package com.example.sky.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    public static String getToken(String key, Long timeout, Map<String, Object> claims) {
        // 获取secretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());

        // 有效时间
        Date expiration = new Date(System.currentTimeMillis() + timeout);

        // 计算得到token
        String token = Jwts.builder()
                .signWith(secretKey)
                .claims(claims)
                .expiration(expiration)
                .compact();
        // 返回token
        return token;
    }

    public static Claims parseToken(String key, String token) {
        // 获取secretKey
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes());
        // 解析token
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }
}
