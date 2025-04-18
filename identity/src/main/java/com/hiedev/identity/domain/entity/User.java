package com.hiedev.identity.domain.entity;

import com.hiedev.identity.domain.enums.UserStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_email", columnList = "email", unique = true)
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String email;

    String otp;
    LocalDateTime otpExpiryDate; // Thời gian hết hạn OTP
    boolean emailVerified; // Trạng thái xác minh email

    String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    UserStatusEnum status = UserStatusEnum.INACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = UserStatusEnum.INACTIVE;
        }
    }
}
