package com.foodwagon.backend.service;

import com.foodwagon.backend.config.JwtService;
import com.foodwagon.backend.dto.auth.AuthResponse;
import com.foodwagon.backend.dto.auth.UserResponse;
import com.foodwagon.backend.dto.partner.PartnerLoginRequest;
import com.foodwagon.backend.dto.partner.PartnerRegisterRequest;
import com.foodwagon.backend.entity.Restaurant;
import com.foodwagon.backend.entity.User;
import com.foodwagon.backend.enums.UserRole;
import com.foodwagon.backend.repository.RestaurantRepository;
import com.foodwagon.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(PartnerRegisterRequest r) {

        if (userRepository.findByEmail(r.email()).isPresent()) {
            return null;
        }

        User partner = User.builder()
                .name(r.ownerName())
                .email(r.email())
                .phone(r.phone())
                .password(passwordEncoder.encode(r.password()))
                .role(UserRole.PARTNER)
                .build();

        User savedUser = userRepository.save(partner);

        Restaurant restaurant = Restaurant.builder()
                .name(r.restaurantName())
                .city(r.city())
                .location(r.address())
                .cuisines(r.cuisines())
                .imageUrl(r.imageUrl())
                .costForTwo(r.costForTwo())
                .online(true)
                .ownerId(savedUser.getId())

                .build();

        restaurantRepository.save(restaurant);

        UserResponse userResponse = new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );

        String token = jwtService.generateToken(savedUser.getEmail());

        return new AuthResponse(token, userResponse);
    }

    public AuthResponse login(PartnerLoginRequest r) {

        User user = userRepository.findByEmail(r.email()).orElse(null);

        if (user == null
                || !passwordEncoder.matches(r.password(), user.getPassword())
                || user.getRole() != UserRole.PARTNER) {
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



    public Restaurant getRestaurant(Long userId) {
        return restaurantRepository.findByOwnerId(userId).orElse(null);
    }
}