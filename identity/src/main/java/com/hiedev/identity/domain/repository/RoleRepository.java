package com.hiedev.identity.domain.repository;

import com.hiedev.identity.domain.entity.Role;
import com.hiedev.identity.domain.enums.RoleEnum;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByRoleName(RoleEnum roleName);
}
