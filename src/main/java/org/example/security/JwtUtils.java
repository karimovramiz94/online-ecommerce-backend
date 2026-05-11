package org.example.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Секретный ключ (в реальном проекте лучше выносить в application.properties)
    private String jwtSecret = "mySecretKeymySecretKeymySecretKeymySecretKeymySecretKeymySecretKey";

    // Время жизни токена (например, 24 часа в миллисекундах)
    private int jwtExpirationMs = 86400000;

    // 1. ГЕНЕРАЦИЯ ТОКЕНА
    public String generateJwtToken(Authentication authentication) {

        // Достаем principal и приводим его к UserDetails
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // Имя пользователя как Subject
                .setIssuedAt(new Date()) // Дата создания
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Дата истечения
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Алгоритм шифрования и ключ
                .compact();
    }

    // 2. ПОЛУЧЕНИЕ ИМЕНИ ИЗ ТОКЕНА
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 3. ПРОВЕРКА ТОКЕНА (Валидация)
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}