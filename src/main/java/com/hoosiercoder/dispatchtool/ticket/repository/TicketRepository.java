package com.hoosiercoder.dispatchtool.ticket.repository;

import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository  extends JpaRepository<Ticket, String> {

    List<Ticket> findByTenantIdAndUser_UserId(String tenantId, Long userId);

    List<Ticket> findByTenantId(String tenantId);

    List<Ticket> findByTenantIdAndUserIsNull(String tenantId);

    Optional<Ticket> findByTenantIdAndTicketId(String tenantId, String ticketId);

    List<Ticket> findByTenantIdAndCreatedDateBetween(String tenantId, java.time.Instant start, java.time.Instant end);

    List<Ticket> findByTenantIdAndStatusAndCreatedDateBetween(String tenantId, TicketStatus status, java.time.Instant start, java.time.Instant end);
}
