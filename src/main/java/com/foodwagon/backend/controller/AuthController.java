package com.foodwagon.backend.controller;

import com.foodwagon.backend.service.AuthService;
import com.foodwagon.backend.dto.auth.*;
import com.foodwagon.backend.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ForgotPasswordService forgotPasswordService;
    // ===============================
    // REGISTER
    // ===============================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        AuthResponse result = authService.register(request);

        if (result == null) {
            return ResponseEntity.status(409).body(
                    new ApiErrorResponse(
                            "User with this email / phone already exists",
                            409,
                            Instant.now()
                    )
            );
        }

        return ResponseEntity.ok(result);
    }

    // ===============================
    // LOGIN
    // ===============================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        AuthResponse result = authService.login(request);

        if (result == null) {
            return ResponseEntity.status(401).body(
                    new ApiErrorResponse(
                            "Invalid email or password",
                            401,
                            Instant.now()
                    )
            );
        }

        return ResponseEntity.ok(result);
    }

    // ===============================
    // FORGOT PASSWORD (Send OTP)
    // ===============================
    @PostMapping("/forgot-password/generate-otp")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        boolean result = forgotPasswordService
                .generateAndSendOtp(request.getEmail());

        if (!result) {
            return ResponseEntity.status(404).body(
                    new ApiErrorResponse(
                            "User not found with this email",
                            404,
                            Instant.now()
                    )
            );
        }

        return ResponseEntity.ok(
                new ApiErrorResponse(
                        "OTP sent successfully to your email",
                        200,
                        Instant.now()
                )
        );
    }

    // ===============================
    // RESET PASSWORD
    // ===============================
    @PostMapping("/forgot-password/reset")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        boolean result = forgotPasswordService.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );

        if (!result) {
            return ResponseEntity.status(400).body(
                    new ApiErrorResponse(
                            "Invalid or expired OTP",
                            400,
                            Instant.now()
                    )
            );
        }

        return ResponseEntity.ok(
                new ApiErrorResponse(
                        "Password reset successful",
                        200,
                        Instant.now()
                )
        );
    }
}