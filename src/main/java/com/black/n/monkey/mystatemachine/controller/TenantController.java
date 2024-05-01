package com.black.n.monkey.mystatemachine.controller;

import com.black.n.monkey.mystatemachine.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TenantController {

  private final TenantService service;

    @GetMapping("/v-jstate/tenant")
    public ResponseEntity<?> findTenants() {
        return ResponseEntity.ok(service.findTenants());
    }
}
