package com.hoosiercoder.dispatchtool.customer.service;

import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDTO> findAllByTenant();
    CustomerDTO createCustomer(CustomerDTO customerDto);
    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(Long id);
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDto);
}
