package com.nicofercavv.user_service.domain.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyerProfile {
    private String id;
    private String keycloakId;
    private String name;
    private String email;
    private String phone;
}
