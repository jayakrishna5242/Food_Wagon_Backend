package com.foodwagon.backend.repository;

import com.foodwagon.backend.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    Optional<PasswordReset> findTopByEmailOrderByIdDesc(String email);
    Optional<PasswordReset> findByEmail(String email);
    Optional<PasswordReset> findTopByEmailOrderByExpiryTimeDesc(String email);}