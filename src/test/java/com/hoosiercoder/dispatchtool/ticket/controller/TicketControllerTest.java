package com.hoosiercoder.dispatchtool.ticket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@WebMvcTest(TicketController.class)
@WithMockUser
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetTickets_thenReturnListOfTickets() throws Exception {
        // Arrange
        TicketDTO ticket = new TicketDTO();
        ticket.setTicketId("TKT-101");
        ticket.setSummary("Test Ticket");

        when(ticketService.listTickets()).thenReturn(List.of(ticket));

        // Act & Assert
        mockMvc.perform(get("/api/v1/ticket")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ticketId", is("TKT-101")));
    }

    @Test
    void whenGetTickets_andNoTicketsExist_thenReturnNoContent() throws Exception {
        // Arrange
        when(ticketService.listTickets()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/ticket")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenCreateTicket_thenReturnCreatedTicket() throws Exception {
        // Arrange
        TicketDTO inputTicket = new TicketDTO();
        inputTicket.setSummary("New Ticket");
        inputTicket.setDescription("Description");

        TicketDTO createdTicket = new TicketDTO();
        createdTicket.setTicketId("TKT-102");
        createdTicket.setSummary("New Ticket");
        createdTicket.setDescription("Description");

        when(ticketService.saveNewTicket(any(TicketDTO.class))).thenReturn(createdTicket);

        // Act & Assert
        mockMvc.perform(post("/api/v1/ticket")
                .with(csrf())
                .header("X-Tenant-ID", "test-tenant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputTicket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId", is("TKT-102")))
                .andExpect(jsonPath("$.summary", is("New Ticket")));
    }

    @Test
    void whenGetTicketById_thenReturnTicket() throws Exception {
        // Arrange
        String ticketId = "TKT-101";
        TicketDTO ticket = new TicketDTO();
        ticket.setTicketId(ticketId);
        ticket.setSummary("Test Ticket");

        when(ticketService.getByTicketId(ticketId)).thenReturn(Optional.of(ticket));

        // Act & Assert
        mockMvc.perform(get("/api/v1/ticket/{id}", ticketId)
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId", is(ticketId)));
    }

    @Test
    void whenGetTicketById_andTicketDoesNotExist_thenReturnNotFound() throws Exception {
        // Arrange
        String ticketId = "TKT-999";
        when(ticketService.getByTicketId(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/ticket/{id}", ticketId)
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isNotFound());
    }
}
