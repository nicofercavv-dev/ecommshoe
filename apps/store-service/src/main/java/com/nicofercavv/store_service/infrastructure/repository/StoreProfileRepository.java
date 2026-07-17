package com.nicofercavv.store_service.infrastructure.repository;

import com.nicofercavv.store_service.infrastructure.entity.StoreProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreProfileRepository extends JpaRepository<StoreProfileEntity, String> {

    /**
     * Busca o perfil da loja através do ID do administrador vindo do Keycloak.
     * Esse método é usado pelo endpoint '/api/stores/me' após o Gateway repassar o header.
     */
    Optional<StoreProfileEntity> findByKeycloakAdminId(String keycloakAdminId);

    /**
     * Verifica se já existe uma loja cadastrada com um determinado CNPJ.
     * Útil para validações de cadastro no Use Case.
     */
    boolean existsByCnpj(String cnpj);
}