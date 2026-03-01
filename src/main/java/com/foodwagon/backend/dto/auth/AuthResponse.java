package com.foodwagon.backend.dto.auth;

public record AuthResponse(
        String token,
        UserResponse user
) {}
