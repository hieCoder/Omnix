package com.hiedev.identity.domain.repository;

import com.hiedev.identity.domain.entity.Provider;

public interface ProviderRepository {
    Provider save(Provider provider);
    boolean existsByProviderId(String providerId);
}
