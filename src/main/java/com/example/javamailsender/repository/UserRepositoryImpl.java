package com.example.javamailsender.repository;

import com.example.javamailsender.model.entity.UserEntity;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory User Repository implementation
 * (Can be replaced with JPA/database implementation later)
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, UserEntity> userStore = new HashMap<>();
    private final Map<String, UserEntity> userEmailIndex = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public UserEntity save(UserEntity user) {
        if (user.getId() == null) {
            user.setId(idCounter++);
        }
        userStore.put(user.getId(), user);
        userEmailIndex.put(user.getEmail(), user);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(userEmailIndex.get(email));
    }

    @Override
    public void delete(Long id) {
        UserEntity user = userStore.get(id);
        if (user != null) {
            userEmailIndex.remove(user.getEmail());
            userStore.remove(id);
        }
    }

    @Override
    public void deleteByEmail(String email) {
        UserEntity user = userEmailIndex.get(email);
        if (user != null) {
            userStore.remove(user.getId());
            userEmailIndex.remove(email);
        }
    }
}
