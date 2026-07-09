package com.nicofercavv.inventory_service.infrastructure.repository;

import com.nicofercavv.inventory_service.infrastructure.entity.InventoryEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InventoryEntity i WHERE i.skuCode = :skuCode")
    Optional<InventoryEntity> findBySkuCodeForUpdate(String skuCode);

    Optional<InventoryEntity> findBySkuCode(String skuCode);
}
