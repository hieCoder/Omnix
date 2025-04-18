package com.hiedev.identity.domain.repository;

import com.hiedev.identity.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Optional<User> findById(Long userId);
}
