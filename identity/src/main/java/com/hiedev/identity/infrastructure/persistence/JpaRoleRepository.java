package com.hiedev.identity.infrastructure.persistence;

import com.hiedev.identity.domain.repository.RoleRepository;
import com.hiedev.identity.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRoleRepository extends JpaRepository<Role, Long>, RoleRepository {
}
