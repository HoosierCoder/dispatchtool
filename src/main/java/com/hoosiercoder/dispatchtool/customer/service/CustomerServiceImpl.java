package com.hoosiercoder.dispatchtool.customer.service;

import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import com.hoosiercoder.dispatchtool.customer.mapper.CustomerMapper;
import com.hoosiercoder.dispatchtool.customer.repository.CustomerRepository;
import com.hoosiercoder.dispatchtool.location.repository.LocationRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: HoosierCoder
 *
 */
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
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);

        // Logic: If a billing address ID was provided, ensure it exists in the DB
        if (customerDto.getBillingAddress() != null && customerDto.getBillingAddress().getId() != null) {
            locationRepository.findById(customerDto.getBillingAddress().getId())
                    .ifPresent(customer::setBillingAddress);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.customerToCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return customerMapper.customerToCustomerDto(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDto) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        existingCustomer.setFirstName(customerDto.getFirstName());
        existingCustomer.setLastName(customerDto.getLastName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPhoneNumber(customerDto.getPhoneNumber());

        // Handle billing address updates if necessary

        return customerMapper.customerToCustomerDto(customerRepository.save(existingCustomer));
    }
}
