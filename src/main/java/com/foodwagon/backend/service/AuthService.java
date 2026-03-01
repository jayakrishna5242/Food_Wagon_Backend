package com.foodwagon.backend.service;

import com.foodwagon.backend.config.JwtService;
import com.foodwagon.backend.dto.auth.AuthResponse;
import com.foodwagon.backend.dto.auth.LoginRequest;
import com.foodwagon.backend.dto.auth.RegisterRequest;
import com.foodwagon.backend.dto.auth.UserResponse;
import com.foodwagon.backend.entity.User;
import com.foodwagon.backend.enums.UserRole;
import com.foodwagon.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ForgotPasswordService forgotPasswordService;


    public AuthResponse register(RegisterRequest r) {

        boolean userExists =
                (r.email() != null && userRepository.findByEmail(r.email()).isPresent())
                        || (!r.phone() .equals("0000000000") && userRepository.findByPhone(r.phone()).isPresent());

        if (userExists) {
            return null;
        }

        User user = User.builder()
                .name(r.name())
                .email(r.email())
                .phone(r.phone())
                .password(passwordEncoder.encode(r.password())) // ✅ only encrypted password
                .role(UserRole.CUSTOMER)
                .build();

        User saved = userRepository.save(user);

        UserResponse userResponse = new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole().name()
        );

        String token = jwtService.generateToken(saved.getEmail());

        return new AuthResponse(token, userResponse);
    }

    public AuthResponse login(LoginRequest r) {

        User user = userRepository.findByEmail(r.identifier())
                .orElseGet(() ->
                        userRepository.findByPhone(r.identifier()).orElse(null)
                );

        if (user == null) {
            return null;
        }

        // BCrypt password check
        if (!passwordEncoder.matches(r.password(), user.getPassword())) {
            return null;
        }

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        );

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, userResponse);
    }
}