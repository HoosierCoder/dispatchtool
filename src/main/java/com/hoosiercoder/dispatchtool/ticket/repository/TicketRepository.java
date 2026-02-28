package com.hoosiercoder.dispatchtool.ticket.repository;

import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TicketRepository  extends JpaRepository<Ticket, String> {

    List<Ticket> findByTenantIdAndUser_UserId(String tenantId, Long userId);

    List<Ticket> findByTenantId(String tenantId);

    List<Ticket> findByTenantIdAndUserIsNull(String tenantId);

    Optional<Ticket> findByTenantIdAndTicketId(String tenantId, String ticketId);
}
