package com.nicofercavv.user_service.infrastructure.web;

import com.nicofercavv.user_service.domain.model.BuyerProfile;
import com.nicofercavv.user_service.domain.usecase.CreateBuyerProfileUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class BuyerController {

    private final CreateBuyerProfileUseCase createBuyerProfileUseCase;

    public BuyerController(CreateBuyerProfileUseCase createBuyerProfileUseCase) {
        this.createBuyerProfileUseCase = createBuyerProfileUseCase;
    }

    @PostMapping("/me")
    public ResponseEntity<BuyerProfile> getOrCreateMyProfile(
            @RequestHeader("X-User-Id") String keycloakId,
            @RequestHeader("X-User-Name") String name,
            @RequestHeader("X-User-Email") String email) {

        BuyerProfile profile = createBuyerProfileUseCase.execute(keycloakId, name, email);
        return ResponseEntity.ok(profile);
    }
}