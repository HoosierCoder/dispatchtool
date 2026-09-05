package com.hoosiercoder.dispatchtool.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@WithMockUser(roles = "SYSTEM_ADMIN")
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenListCustomers_thenReturnCustomerList() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");

        when(customerService.listCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/v1/test-tenant/customers")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].lastName", is("Doe")));
    }

    @Test
    void whenListCustomers_andNoCustomersExist_thenReturnNoContent() throws Exception {
        when(customerService.listCustomers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/test-tenant/customers")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenGetCustomerById_thenReturnCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(1L);
        customer.setFirstName("Jane");
        customer.setLastName("Smith");

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/v1/test-tenant/customers/1")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    void whenGetCustomerById_andNotFound_thenReturn404() throws Exception {
        when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/test-tenant/customers/99")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCreateCustomer_thenReturnCreatedCustomer() throws Exception {
        CustomerDTO input = new CustomerDTO();
        input.setFirstName("Alice");
        input.setLastName("Wonderland");
        input.setEmail("alice@example.com");

        CustomerDTO created = new CustomerDTO();
        created.setId(10L);
        created.setFirstName("Alice");
        created.setLastName("Wonderland");

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/test-tenant/customers")
                        .with(csrf())
                        .header("X-Tenant-ID", "test-tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.lastName", is("Wonderland")));
    }

    @Test
    void whenCreateCustomer_withInvalidDto_thenReturnBadRequest() throws Exception {
        CustomerDTO invalid = new CustomerDTO();
        // Missing mandatory @NotBlank lastName field

        mockMvc.perform(post("/api/v1/test-tenant/customers")
                        .with(csrf())
                        .header("X-Tenant-ID", "test-tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateCustomer_thenReturnUpdatedCustomer() throws Exception {
        CustomerDTO updateInput = new CustomerDTO();
        updateInput.setFirstName("Updated");
        updateInput.setLastName("Name");

        CustomerDTO updatedResult = new CustomerDTO();
        updatedResult.setId(1L);
        updatedResult.setFirstName("Updated");
        updatedResult.setLastName("Name");

        when(customerService.updateCustomer(eq(1L), any(CustomerDTO.class))).thenReturn(updatedResult);

        mockMvc.perform(put("/api/v1/test-tenant/customers/1")
                        .with(csrf())
                        .header("X-Tenant-ID", "test-tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.lastName", is("Name")));
    }

    @Test
    void whenUpdateCustomer_andNotFound_thenReturn404() throws Exception {
        CustomerDTO updateInput = new CustomerDTO();
        updateInput.setLastName("Name");

        when(customerService.updateCustomer(eq(99L), any(CustomerDTO.class)))
                .thenThrow(new RuntimeException("Customer not found"));

        mockMvc.perform(put("/api/v1/test-tenant/customers/99")
                        .with(csrf())
                        .header("X-Tenant-ID", "test-tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInput)))
                .andExpect(status().isNotFound());
    }
}
