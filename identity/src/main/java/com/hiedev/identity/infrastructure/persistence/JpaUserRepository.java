package com.hiedev.identity.infrastructure.persistence;

import com.hiedev.identity.domain.repository.UserRepository;
import com.hiedev.identity.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
}
