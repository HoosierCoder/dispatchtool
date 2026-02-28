package com.hoosiercoder.dispatchtool.ticket.service;

import com.hoosiercoder.dispatchtool.config.ConfigCache;
import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.customer.repository.CustomerRepository;
import com.hoosiercoder.dispatchtool.location.repository.LocationRepository;
import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;
import com.hoosiercoder.dispatchtool.ticket.mapper.TicketMapper;
import com.hoosiercoder.dispatchtool.ticket.repository.TicketRepository;
import com.hoosiercoder.dispatchtool.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Author: HoosierCoder
 *
 */
@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock private TicketMapper ticketMapper;
    @Mock private UserRepository userRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private LocationRepository locationRepository;
    @Mock private ConfigCache configCache;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private final String TENANT_A = "acme-hvac";
    private final String TENANT_B = "globex-plumbing";

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldListOnlyTicketsForCurrentTenant() {
        // Arrange
        TenantContext.setTenantId(TENANT_A);
        Ticket ticket = new Ticket();
        ticket.setTicketId("TKT-101");
        ticket.setTenantId(TENANT_A);

        TicketDTO dto = new TicketDTO();
        dto.setTicketId("TKT-101");

        when(ticketRepository.findByTenantId(TENANT_A)).thenReturn(List.of(ticket));
        when(ticketMapper.ticketToTicketDto(ticket)).thenReturn(dto);

        // Act
        List<TicketDTO> results = ticketService.listTickets();

        // Assert
        assertEquals(1, results.size());
        assertEquals("TKT-101", results.get(0).getTicketId());
        verify(ticketRepository).findByTenantId(TENANT_A);
        verify(ticketRepository, never()).findAll(); // Ensure we aren't using the dangerous method
    }

    @Test
    void shouldPreventAccessToTicketFromAnotherTenant() {
        // Arrange
        TenantContext.setTenantId(TENANT_A);
        String targetTicketId = "TKT-999";

        // Simulate that this ticket exists in DB but belongs to TENANT_B
        // The service uses findByTenantIdAndTicketId, so it should return empty
        when(ticketRepository.findByTenantIdAndTicketId(TENANT_A, targetTicketId))
                .thenReturn(Optional.empty());

        // Act
        Optional<TicketDTO> result = ticketService.getByTicketId(targetTicketId);

        // Assert
        assertTrue(result.isEmpty());
        verify(ticketRepository).findByTenantIdAndTicketId(TENANT_A, targetTicketId);
        verifyNoInteractions(ticketMapper);
    }

    @Test
    void shouldFilterUnassignedTicketsByTenant() {
        // Arrange
        TenantContext.setTenantId(TENANT_B);
        Ticket unassignedTicket = new Ticket();
        unassignedTicket.setStatus(TicketStatus.UNASSIGNED);
        unassignedTicket.setTenantId(TENANT_B);

        when(ticketRepository.findByTenantIdAndUserIsNull(TENANT_B))
                .thenReturn(List.of(unassignedTicket));
        when(ticketMapper.ticketToTicketDto(any())).thenReturn(new TicketDTO());

        // Act
        ticketService.findUnassignedTickets();

        // Assert
        verify(ticketRepository).findByTenantIdAndUserIsNull(TENANT_B);
    }

    @Test
    void shouldSetTenantIdAutomaticallyOnCreation() {
        // Arrange
        TenantContext.setTenantId(TENANT_A);
        TicketDTO inputDto = new TicketDTO();
        inputDto.setSummary("Leaking Pipe");

        Ticket entity = new Ticket();
        entity.setSummary("Leaking Pipe");

        when(ticketMapper.ticketDtoToTicket(inputDto)).thenReturn(entity);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);
        when(ticketMapper.ticketToTicketDto(any(Ticket.class))).thenReturn(new TicketDTO());

        // Act
        ticketService.saveNewTicket(inputDto);

        // Assert
        assertEquals(TENANT_A, entity.getTenantId(), "The service must force the Tenant ID from the Context");
        verify(ticketRepository).save(entity);
    }
}
