package com.nicofercavv.user_service.domain.usecase;

import com.nicofercavv.user_service.domain.model.BuyerProfile;
import com.nicofercavv.user_service.infrastructure.entity.BuyerProfileEntity;
import com.nicofercavv.user_service.infrastructure.repository.BuyerProfileRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateBuyerProfileUseCase {

    private final BuyerProfileRepository repository;

    public CreateBuyerProfileUseCase(BuyerProfileRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BuyerProfile execute(String keycloakId, String name, String email) {
        return repository.findByKeycloakId(keycloakId)
                .map(entity -> BuyerProfile.builder()
                        .id(entity.getId())
                        .keycloakId(entity.getKeycloakId())
                        .name(entity.getName())
                        .email(entity.getEmail())
                        .phone(entity.getPhone())
                        .build())
                .orElseGet(() -> {
                    // Se não existir, cria o perfil do comprador no banco local
                    var newEntity = BuyerProfileEntity.builder()
                            .keycloakId(keycloakId)
                            .name(name)
                            .email(email)
                            .build();
                    var saved = repository.save(newEntity);
                    return BuyerProfile.builder()
                            .id(saved.getId())
                            .keycloakId(saved.getKeycloakId())
                            .name(saved.getName())
                            .email(saved.getEmail())
                            .build();
                });
    }
}