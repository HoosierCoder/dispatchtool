package com.hoosiercoder.dispatchtool.tenant.controller;

import com.hoosiercoder.dispatchtool.tenant.dto.TenantDTO;
import com.hoosiercoder.dispatchtool.tenant.service.TenantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author: HoosierCoder
 *
 */
@RestController
@RequestMapping("/api/v1/admin/tenants")
public class TenantController {
    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping
    public ResponseEntity<TenantDTO> registerNewCompany(@Valid @RequestBody TenantDTO tenantDto) {
        return new ResponseEntity<>(tenantService.createTenant(tenantDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TenantDTO>> getAllClients() {
        List<TenantDTO> tenants = tenantService.listAllTenants();
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<TenantDTO> getTenant(@PathVariable String tenantId) {
        return tenantService.getTenantById(tenantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
