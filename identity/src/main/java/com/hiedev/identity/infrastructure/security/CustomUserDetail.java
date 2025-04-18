package com.hiedev.identity.infrastructure.security;

import com.hiedev.identity.domain.entity.Permission;
import com.hiedev.identity.domain.entity.Role;
import com.hiedev.identity.domain.entity.User;
import com.hiedev.identity.domain.enums.PermissionEnum;
import com.hiedev.identity.domain.enums.RoleEnum;
import com.hiedev.identity.domain.enums.UserStatusEnum;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetail implements UserDetails {

    String email;
    Set<RoleEnum> roles;
    UserStatusEnum status;
    String password;
    Set<PermissionEnum> permissions;

    public CustomUserDetail(User user) {
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());
        this.status = user.getStatus();
        this.password = user.getPassword();
        this.permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getPermissionName)
                .collect(Collectors.toSet());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(
                role -> new SimpleGrantedAuthority(role.name())
        ).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatusEnum.DELETE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status != UserStatusEnum.INACTIVE;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public Set<RoleEnum> getRoles() {
        return roles;
    }

    public Set<PermissionEnum> getPermissions() {
        return permissions;
    }
}
