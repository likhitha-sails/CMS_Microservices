package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String register(User user) {
        log.info(" Register request received for username: {}", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info(" User registered successfully: {}", user.getUsername());
        return "User registered successfully!";
    }

    public String login(String username, String password) {
        log.info("Login attempt for username: {}", username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            log.info("Authentication successful for user: {}", username);

            String token = jwtUtil.generateToken(username);
            log.info(" JWT generated for user {}: {}", username, token);

            return token;
        } catch (AuthenticationException e) {
            log.error(" Authentication failed for user: {} — Reason: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(" Unexpected error during login for user: {}", username, e);
            throw e;
        }
    }
}
