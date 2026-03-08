package com.hoosiercoder.dispatchtool.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import com.hoosiercoder.dispatchtool.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser(roles = "SYSTEM_ADMIN")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetUsers_thenReturnListOfUsers() throws Exception {
        // Arrange
        UserDTO user = new UserDTO();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userService.listUsers()).thenReturn(List.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")));
    }

    @Test
    void whenGetUsers_andNoUsersExist_thenReturnNoContent() throws Exception {
        // Arrange
        when(userService.listUsers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/users")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenCreateUser_thenReturnCreatedUser() throws Exception {
        // Arrange
        UserDTO inputUser = new UserDTO();
        inputUser.setFirstName("Jane");
        inputUser.setLastName("Doe");
        inputUser.setUserRole(UserRole.ASSOCIATE);

        UserDTO createdUser = new UserDTO();
        createdUser.setUserId(2L);
        createdUser.setFirstName("Jane");
        createdUser.setLastName("Doe");
        createdUser.setUserRole(UserRole.ASSOCIATE);

        when(userService.saveNewUser(any(UserDTO.class))).thenReturn(createdUser);

        // Act & Assert
        mockMvc.perform(post("/api/v1/users")
                        .with(csrf())
                        .header("X-Tenant-ID", "test-tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(2)))
                .andExpect(jsonPath("$.firstName", is("Jane")));
    }
}
