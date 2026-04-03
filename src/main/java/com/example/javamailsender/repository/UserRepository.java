package com.example.javamailsender.repository;

import com.example.javamailsender.model.entity.UserEntity;
import java.util.Optional;

/**
 * User Repository - stores and retrieves user records
 */
public interface UserRepository {
    UserEntity save(UserEntity user);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByEmail(String email);
    void delete(Long id);
    void deleteByEmail(String email);
}
