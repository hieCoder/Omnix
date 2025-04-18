package com.hiedev.identity.domain.entity;

import com.hiedev.identity.domain.enums.UserProviderEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "providers",
        indexes = {
                @Index(name = "idx_provider_provider_id", columnList = "provider, provider_id", unique = true) // Index cho cặp provider và provider_id
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "providerId", nullable = false, unique = true)
    String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserProviderEnum provider;

}
