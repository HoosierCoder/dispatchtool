package com.hoosiercoder.dispatchtool.customer.service;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import com.hoosiercoder.dispatchtool.customer.mapper.CustomerMapper;
import com.hoosiercoder.dispatchtool.customer.repository.CustomerRepository;
import com.hoosiercoder.dispatchtool.location.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: HoosierCoder
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final LocationRepository locationRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerMapper customerMapper,
                               LocationRepository locationRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.locationRepository = locationRepository;
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDto) {
        String tenantId = TenantContext.getTenantId();
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);

        // Verify billing location belongs to this tenant before attaching
        if (customerDto.getBillingAddress() != null && customerDto.getBillingAddress().getId() != null) {
            locationRepository.findByTenantIdAndId(tenantId, customerDto.getBillingAddress().getId())
                    .ifPresent(customer::setBillingAddress);
        }

        customer.setTenantId(tenantId);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        String tenantId = TenantContext.getTenantId();

        // Scope lookups to the tenant
        Customer customer = customerRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Customer not found or access denied"));
        return customerMapper.customerToCustomerDto(customer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        String tenantId = TenantContext.getTenantId();

        return customerRepository.findByTenantId(tenantId).stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDto) {
        String tenantId = TenantContext.getTenantId();

        // Ensure we only update a customer belonging to the requester
        Customer existingCustomer = customerRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Customer not found or access denied"));

        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPhoneNumber(customerDto.getPhoneNumber());

        // Securely update billing address
        if (customerDto.getBillingAddress() != null && customerDto.getBillingAddress().getId() != null) {
            locationRepository.findByTenantIdAndId(tenantId, customerDto.getBillingAddress().getId())
                    .ifPresent(existingCustomer::setBillingAddress);
        }

        return customerMapper.customerToCustomerDto(customerRepository.save(existingCustomer));
    }
}
