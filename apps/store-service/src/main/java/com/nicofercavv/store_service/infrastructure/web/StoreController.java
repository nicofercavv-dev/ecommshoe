package com.nicofercavv.store_service.infrastructure.web;

import com.nicofercavv.store_service.infrastructure.entity.StoreProfileEntity;
import com.nicofercavv.store_service.infrastructure.repository.StoreProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreProfileRepository repository;

    public StoreController(StoreProfileRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/me")
    public ResponseEntity<StoreProfileEntity> getMyStore(@RequestHeader("X-User-Id") String keycloakAdminId) {
        return repository.findByKeycloakAdminId(keycloakAdminId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}