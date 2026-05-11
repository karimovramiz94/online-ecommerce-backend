package org.example.controller;

import org.example.security.JwtResponse;
import org.example.security.JwtUtils;
import org.example.User; // Твоя сущность User
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User loginRequest) {

        // 1. Проверяем логин и пароль (AuthenticationManager сам сходит в БД)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Устанавливаем аутентификацию в контекст
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Генерируем токен
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Возвращаем токен клиенту
        return ResponseEntity.ok(new JwtResponse(jwt, loginRequest.getUsername()));
    }
}