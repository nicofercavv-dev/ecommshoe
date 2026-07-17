package com.nicofercavv.store_service.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "store_profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "keycloak_admin_id", unique = true, nullable = false)
    private String keycloakAdminId;

    @Column(name = "corporate_name", nullable = false)
    private String corporateName;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(name = "commission_rate", nullable = false)
    private BigDecimal commissionRate;
}