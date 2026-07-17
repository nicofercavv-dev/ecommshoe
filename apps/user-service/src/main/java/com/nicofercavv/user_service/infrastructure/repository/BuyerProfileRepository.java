package com.nicofercavv.user_service.infrastructure.repository;

import com.nicofercavv.user_service.infrastructure.entity.BuyerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuyerProfileRepository extends JpaRepository<BuyerProfileEntity, String> {
    Optional<BuyerProfileEntity> findByKeycloakId(String keycloakId);
}
