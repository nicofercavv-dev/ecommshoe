package com.nicofercavv.store_service.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreProfile {
    private String id;
    private String keycloakAdminId;
    private String corporateName;
    private String cnpj;
    private BigDecimal commissionRate;
}