package com.hoosiercoder.dispatchtool.customer.controller;

import com.hoosiercoder.dispatchtool.config.security.WithMockDispatchUser;
import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerWebController.class)
@WithMockDispatchUser(tenantId = "test-tenant")
public class CustomerWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void whenListCustomers_shouldReturnMainLayoutWithCustomerList() throws Exception {
        CustomerDTO c1 = new CustomerDTO();
        c1.setId(1L);
        c1.setFirstName("Bob");
        c1.setLastName("Builder");

        when(customerService.listCustomers()).thenReturn(List.of(c1));

        mockMvc.perform(get("/tenant/test-tenant/customers"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout/main"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attribute("tenantId", "test-tenant"))
                .andExpect(model().attribute("content", "customer/list"));
    }

    @Test
    void whenShowCreateForm_shouldReturnMainLayoutWithNewCustomerModel() throws Exception {
        mockMvc.perform(get("/tenant/test-tenant/customers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout/main"))
                .andExpect(model().attributeExists("customer"))
                .andExpect(model().attribute("isEdit", false))
                .andExpect(model().attribute("content", "customer/form"));
    }

    @Test
    void whenCreateCustomer_withValidData_shouldRedirectToCustomerList() throws Exception {
        mockMvc.perform(post("/tenant/test-tenant/customers/new")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenant/test-tenant/customers"))
                .andExpect(flash().attributeExists("message"));
    }

    @Test
    void whenCreateCustomer_withBindingErrors_shouldReRenderForm() throws Exception {
        mockMvc.perform(post("/tenant/test-tenant/customers/new")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "")) // Missing required lastName
                .andExpect(status().isOk())
                .andExpect(view().name("layout/main"))
                .andExpect(model().attribute("content", "customer/form"));
    }

    @Test
    void whenShowEditForm_andCustomerExists_shouldReturnFormView() throws Exception {
        CustomerDTO customer = new CustomerDTO();
        customer.setId(1L);
        customer.setLastName("Doe");

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/tenant/test-tenant/customers/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout/main"))
                .andExpect(model().attribute("isEdit", true))
                .andExpect(model().attribute("content", "customer/form"));
    }

    @Test
    void whenShowEditForm_andCustomerNotFound_shouldRedirectToList() throws Exception {
        when(customerService.getCustomerById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tenant/test-tenant/customers/99/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenant/test-tenant/customers"));
    }

    @Test
    void whenUpdateCustomer_withValidData_shouldRedirectWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/tenant/test-tenant/customers/1/edit")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Updated"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenant/test-tenant/customers"))
                .andExpect(flash().attribute("message", "Customer updated successfully!"));
    }

    @Test
    void whenUpdateCustomer_andServiceThrowsException_shouldRedirectWithErrorMessage() throws Exception {
        doThrow(new RuntimeException("Not found"))
                .when(customerService).updateCustomer(eq(99L), any(CustomerDTO.class));

        mockMvc.perform(post("/tenant/test-tenant/customers/99/edit")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenant/test-tenant/customers"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void whenDeleteCustomer_shouldRedirectWithSuccessMessage() throws Exception {
        mockMvc.perform(post("/tenant/test-tenant/customers/1/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenant/test-tenant/customers"))
                .andExpect(flash().attribute("message", "Customer deleted successfully!"));
    }

    @Test
    void whenDeleteCustomer_andServiceThrowsException_shouldRedirectWithErrorMessage() throws Exception {
        doThrow(new RuntimeException("Could not delete"))
                .when(customerService).deleteCustomer(99L);

        mockMvc.perform(post("/tenant/test-tenant/customers/99/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tenant/test-tenant/customers"))
                .andExpect(flash().attributeExists("error"));
    }
}
