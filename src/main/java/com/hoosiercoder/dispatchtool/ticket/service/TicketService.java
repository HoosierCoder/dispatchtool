package com.hoosiercoder.dispatchtool.ticket.service;

import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    TicketDTO saveNewTicket(TicketDTO ticketDTO);

    List<TicketDTO> findByUser(Long userId);

    List<TicketDTO> findUnassignedTickets();

    Optional<TicketDTO> getByTicketId(String ticketId);

    List<TicketDTO> listTickets();

    List<TicketDTO> findTicketsByRange(java.time.Instant start, java.time.Instant end);

    List<TicketDTO> findTicketsByRangeAndStatus(java.time.Instant start, java.time.Instant end, TicketStatus status);
}
