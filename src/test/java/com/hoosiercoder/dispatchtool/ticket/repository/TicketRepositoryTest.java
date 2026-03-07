package com.hoosiercoder.dispatchtool.ticket.repository;

import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TicketRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        // Create Tenants that our Tickets will reference
        Tenant tenantA = new Tenant();
        tenantA.setTenantId("tenant-a");
        tenantA.setCompanyName("Company A");
        tenantA.setPrimaryContactName("Contact A");
        tenantA.setPrimaryContactEmail("contact@companya.com");
        entityManager.persist(tenantA);

        Tenant tenantB = new Tenant();
        tenantB.setTenantId("tenant-b");
        tenantB.setCompanyName("Company B");
        tenantB.setPrimaryContactName("Contact B");
        tenantB.setPrimaryContactEmail("contact@companyb.com");
        entityManager.persist(tenantB);
    }

    @Test
    public void whenFindByTenantId_thenReturnOnlyTicketsForThatTenant() {
        // Arrange
        Ticket ticket1 = new Ticket();
        ticket1.setTicketId("TKT-1");
        ticket1.setSummary("Ticket for Tenant A");
        ticket1.setTenantId("tenant-a");
        entityManager.persist(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setTicketId("TKT-2");
        ticket2.setSummary("Ticket for Tenant B");
        ticket2.setTenantId("tenant-b");
        entityManager.persist(ticket2);

        entityManager.flush();

        // Act
        List<Ticket> found = ticketRepository.findByTenantId("tenant-a");

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTicketId()).isEqualTo("TKT-1");
    }

    @Test
    public void whenFindByTenantIdAndTicketId_thenReturnTicket() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setTicketId("TKT-3");
        ticket.setSummary("Specific Ticket");
        ticket.setTenantId("tenant-a");
        entityManager.persist(ticket);
        entityManager.flush();

        // Act
        Optional<Ticket> found = ticketRepository.findByTenantIdAndTicketId("tenant-a", "TKT-3");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getSummary()).isEqualTo("Specific Ticket");
    }

    @Test
    public void whenFindByTenantIdAndTicketId_withWrongTenant_thenReturnEmpty() {
        // Arrange
        Ticket ticket = new Ticket();
        ticket.setTicketId("TKT-4");
        ticket.setSummary("Ticket for Tenant A");
        ticket.setTenantId("tenant-a");
        entityManager.persist(ticket);
        entityManager.flush();

        // Act
        Optional<Ticket> found = ticketRepository.findByTenantIdAndTicketId("tenant-b", "TKT-4");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    public void whenFindByTenantIdAndCreatedDateBetween_thenReturnTickets() {
        // Arrange
        Instant now = Instant.now();
        
        Ticket ticket1 = new Ticket();
        ticket1.setTicketId("TKT-5");
        ticket1.setSummary("Recent Ticket");
        ticket1.setTenantId("tenant-a");
        entityManager.persist(ticket1);
        entityManager.flush();

        // Act
        List<Ticket> found = ticketRepository.findByTenantIdAndCreatedDateBetween(
                "tenant-a", 
                now.minus(1, ChronoUnit.HOURS), 
                now.plus(1, ChronoUnit.HOURS)
        );

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTicketId()).isEqualTo("TKT-5");
    }

    @Test
    public void whenFindByTenantIdAndStatusAndCreatedDateBetween_thenReturnMatchingTickets() {
        // Arrange
        Instant now = Instant.now();

        Ticket ticket1 = new Ticket();
        ticket1.setTicketId("TKT-6");
        ticket1.setSummary("Open Ticket");
        ticket1.setTenantId("tenant-a");
        ticket1.setStatus(TicketStatus.ASSIGNED);
        entityManager.persist(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setTicketId("TKT-7");
        ticket2.setSummary("Closed Ticket");
        ticket2.setTenantId("tenant-a");
        ticket2.setStatus(TicketStatus.CLOSED);
        entityManager.persist(ticket2);

        entityManager.flush();

        // Act
        List<Ticket> found = ticketRepository.findByTenantIdAndStatusAndCreatedDateBetween(
                "tenant-a",
                TicketStatus.ASSIGNED,
                now.minus(1, ChronoUnit.HOURS),
                now.plus(1, ChronoUnit.HOURS)
        );

        // Assert
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTicketId()).isEqualTo("TKT-6");
    }
}
