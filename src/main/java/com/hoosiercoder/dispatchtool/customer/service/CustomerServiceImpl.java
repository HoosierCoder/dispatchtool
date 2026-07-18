package com.hoosiercoder.dispatchtool.customer.service;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import com.hoosiercoder.dispatchtool.customer.mapper.CustomerMapper;
import com.hoosiercoder.dispatchtool.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDTO> findAllByTenant() {
        String tenantId = TenantContext.getTenantId();
        return customerRepository.findByTenantId(tenantId).stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDto) {
        String tenantId = TenantContext.getTenantId();
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        customer.setTenantId(tenantId);
        
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return findAllByTenant();
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long id) {
        String tenantId = TenantContext.getTenantId();
        return customerRepository.findByTenantIdAndId(tenantId, id)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDto) {
        String tenantId = TenantContext.getTenantId();
        Customer existingCustomer = customerRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Map updates (basic example, might want selective updates in real world)
        // Ideally, mapper should have update methods
        Customer updatedCustomer = customerMapper.customerDtoToCustomer(customerDto);
        updatedCustomer.setId(existingCustomer.getId()); // preserve ID
        updatedCustomer.setTenantId(tenantId); // preserve Tenant
        
        // Note: Ideally you'd copy fields from updatedCustomer to existingCustomer to avoid losing relations not in DTO
        // For now, assuming DTO has everything needed or full replace is OK
        
        Customer saved = customerRepository.save(updatedCustomer);
        return customerMapper.customerToCustomerDto(saved);
    }

    @Override
    public void deleteCustomer(Long id) {
        String tenantId = TenantContext.getTenantId();
        // Verify the customer belongs to the current tenant before deleting
        customerRepository.findByTenantIdAndId(tenantId, id)
                .ifPresentOrElse(
                    customer -> customerRepository.deleteById(id),
                    () -> { throw new RuntimeException("Customer not found or access denied."); }
                );
    }
}
