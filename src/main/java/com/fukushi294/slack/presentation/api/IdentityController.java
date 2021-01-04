package com.fukushi294.slack.presentation.api;

import com.fukushi294.slack.application.IdentityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdentityController {
    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PutMapping("/v1/api/{id}")
    public ResponseEntity<Object> register(@PathVariable String id) {
        identityService.registerSlackUserIdentity(id);
        return ResponseEntity.noContent().build();
    }
}
