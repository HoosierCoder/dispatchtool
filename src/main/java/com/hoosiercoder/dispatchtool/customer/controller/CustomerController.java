package com.hoosiercoder.dispatchtool.customer.controller;

import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Author: HoosierCoder
 *
 */
@RestController
@RequestMapping("/api/v1/{tenantId}/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@PathVariable String tenantId, @Valid @RequestBody CustomerDTO customerDto) {
        // The service will use the TenantContext, which will be set by our filter
        CustomerDTO createdCustomer = customerService.createCustomer(customerDto);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> listCustomers(@PathVariable String tenantId) {
        List<CustomerDTO> customers = customerService.listCustomers();

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String tenantId, @PathVariable Long id) {
        Optional<CustomerDTO> customer = customerService.getCustomerById(id);
        
        return customer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable String tenantId, @PathVariable Long id, @Valid @RequestBody CustomerDTO customerDto) {
        try {
            return ResponseEntity.ok(customerService.updateCustomer(id, customerDto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
