package com.andabazaar.repository;

import com.andabazaar.enums.UserStatus;
import com.andabazaar.repository.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    long countByStatus(UserStatus status);
}
