package com.hiedev.identity.infrastructure.persistence;

import com.hiedev.identity.domain.repository.ProviderRepository;
import com.hiedev.identity.domain.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProviderRepository extends JpaRepository<Provider, Long>, ProviderRepository {

}
