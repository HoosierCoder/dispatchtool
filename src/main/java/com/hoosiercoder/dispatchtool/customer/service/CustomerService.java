package com.hoosiercoder.dispatchtool.customer.service;

import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import java.util.List;

/**
 * Author: HoosierCoder
 */
public interface CustomerService {

    // Creates a customer for the current tenant in context
    CustomerDTO createCustomer(CustomerDTO customerDto);

    // Retrieves a customer only if they belong to the current tenant
    CustomerDTO getCustomerById(Long id);

    // Returns all customers belonging to the current tenant
    List<CustomerDTO> listCustomers();

    // Updates a customer record within the tenant's scope
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDto);
}