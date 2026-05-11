package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Извлекаем JWT токен из заголовка Authorization
            String jwt = parseJwt(request);

            // 2. Если токен есть и он валидный
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // 3. Достаем имя пользователя из токена
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4. Загружаем данные пользователя из базы данных
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Создаем объект аутентификации
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Устанавливаем пользователя в контекст Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Не удалось установить аутентификацию пользователя: {}", e.getMessage());
        }

        // Продолжаем выполнение цепочки фильтров
        filterChain.doFilter(request, response);
    }

    // Вспомогательный метод для парсинга заголовка "Authorization: Bearer <token>"
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Убираем слово "Bearer " и берем сам токен
        }

        return null;
    }
}