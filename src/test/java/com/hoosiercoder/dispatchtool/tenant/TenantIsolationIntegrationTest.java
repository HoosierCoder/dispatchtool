package com.hoosiercoder.dispatchtool.tenant;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import com.hoosiercoder.dispatchtool.customer.repository.CustomerRepository;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.ticket.repository.TicketRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TenantIsolationIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void setUp() {
        Tenant tenantA = new Tenant();
        tenantA.setTenantId("tenant-alpha");
        tenantA.setCompanyName("Alpha Corp");
        tenantA.setPrimaryContactName("Alpha Admin");
        tenantA.setPrimaryContactEmail("admin@alpha.com");
        entityManager.persist(tenantA);

        Tenant tenantB = new Tenant();
        tenantB.setTenantId("tenant-beta");
        tenantB.setCompanyName("Beta Corp");
        tenantB.setPrimaryContactName("Beta Admin");
        tenantB.setPrimaryContactEmail("admin@beta.com");
        entityManager.persist(tenantB);

        entityManager.flush();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    public void whenQueryingAsTenantAlpha_shouldNotSeeTenantBetaData() {
        // Arrange - Persist data belonging to tenant-alpha
        Customer customerA = new Customer();
        customerA.setTenantId("tenant-alpha");
        customerA.setFirstName("Customer");
        customerA.setLastName("Alpha");
        customerA.setEmail("cust@alpha.com");
        customerA.setPhoneNumber("555-0100");
        entityManager.persist(customerA);

        Location locationA = new Location();
        locationA.setTenantId("tenant-alpha");
        locationA.setStreetAddress("100 Alpha St");
        locationA.setCity("Indianapolis");
        locationA.setState("IN");
        locationA.setZipCode("46204");
        entityManager.persist(locationA);

        Ticket ticketA = new Ticket();
        ticketA.setTicketId("TKT-ALPHA-1");
        ticketA.setTenantId("tenant-alpha");
        ticketA.setSummary("Alpha Service Ticket");
        ticketA.setCustomer(customerA);
        ticketA.setLocation(locationA);
        entityManager.persist(ticketA);

        // Arrange - Persist data belonging to tenant-beta
        Customer customerB = new Customer();
        customerB.setTenantId("tenant-beta");
        customerB.setFirstName("Customer");
        customerB.setLastName("Beta");
        customerB.setEmail("cust@beta.com");
        customerB.setPhoneNumber("555-0200");
        entityManager.persist(customerB);

        Ticket ticketB = new Ticket();
        ticketB.setTicketId("TKT-BETA-1");
        ticketB.setTenantId("tenant-beta");
        ticketB.setSummary("Beta Service Ticket");
        ticketB.setCustomer(customerB);
        entityManager.persist(ticketB);

        entityManager.flush();

        // Act - Query repositories explicitly scoped to tenant-alpha
        List<Customer> alphaCustomers = customerRepository.findByTenantId("tenant-alpha");
        List<Ticket> alphaTickets = ticketRepository.findByTenantId("tenant-alpha");
        Optional<Ticket> betaTicketAttempt = ticketRepository.findByTenantIdAndTicketId("tenant-alpha", "TKT-BETA-1");

        // Assert - Verify tenant-alpha cannot see tenant-beta data
        assertThat(alphaCustomers).hasSize(1);
        assertThat(alphaCustomers.get(0).getLastName()).isEqualTo("Alpha");

        assertThat(alphaTickets).hasSize(1);
        assertThat(alphaTickets.get(0).getTicketId()).isEqualTo("TKT-ALPHA-1");

        assertThat(betaTicketAttempt).isEmpty();
    }

    @Test
    public void whenTenantContextIsCleared_shouldReturnNull() {
        TenantContext.setTenantId("tenant-alpha");
        assertThat(TenantContext.getTenantId()).isEqualTo("tenant-alpha");

        TenantContext.clear();
        assertThat(TenantContext.getTenantId()).isNull();
    }
}
