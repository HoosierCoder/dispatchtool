package com.hoosiercoder.dispatchtool.customer.service;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import com.hoosiercoder.dispatchtool.customer.mapper.CustomerMapper;
import com.hoosiercoder.dispatchtool.customer.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Author: HoosierCoder
 *
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @AfterEach
    void tearDown() {
        // Essential: Clear the context so one test doesn't pollute the next
        TenantContext.clear();
    }

    @Test
    void shouldReturnOnlyTenantsCustomers() {
        // Arrange
        String tenantId = "seattle-dispatch-hub";
        TenantContext.setTenantId(tenantId);

        Customer customer = new Customer();
        customer.setFirstName("Lia");
        customer.setTenantId(tenantId);

        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName("Lia");

        when(customerRepository.findByTenantId(tenantId)).thenReturn(List.of(customer));
        when(customerMapper.customerToCustomerDto(customer)).thenReturn(dto);

        // Act
        List<CustomerDTO> results = customerService.listCustomers();

        // Assert
        assertEquals(1, results.size());
        assertEquals("Lia", results.get(0).getFirstName());
        verify(customerRepository).findByTenantId(tenantId);
    }

    @Test
    void shouldThrowExceptionWhenAccessingCustomerOfDifferentTenant() {
        // Arrange
        String attackerTenant = "malicious-corp";
        String victimTenant = "seattle-dispatch-hub";
        Long targetCustomerId = 999L;

        // The "Attacker" is logged in
        TenantContext.setTenantId(attackerTenant);

        // Even if the ID exists in the DB, the query includes the tenantId
        // so the repository will return empty for this specific combination
        when(customerRepository.findByTenantIdAndId(attackerTenant, targetCustomerId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(targetCustomerId);
        });

        assertTrue(exception.getMessage().contains("access denied"));
        verify(customerRepository).findByTenantIdAndId(attackerTenant, targetCustomerId);
        verifyNoInteractions(customerMapper); // Should never even try to map the data
    }

    @Test
    void shouldCreateCustomerWithCorrectTenantId() {
        // Arrange
        String tenantId = "seattle-dispatch-hub";
        TenantContext.setTenantId(tenantId);

        CustomerDTO inputDto = new CustomerDTO();
        inputDto.setFirstName("Jules");

        Customer customerEntity = new Customer();
        customerEntity.setFirstName("Jules");

        when(customerMapper.customerDtoToCustomer(inputDto)).thenReturn(customerEntity);
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArguments()[0]);
        when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(inputDto);

        // Act
        customerService.createCustomer(inputDto);

        // Assert
        assertEquals(tenantId, customerEntity.getTenantId());
        verify(customerRepository).save(customerEntity);
    }
}
