package com.hoosiercoder.dispatchtool.ticket.service;

import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    public TicketDTO saveNewTicket(TicketDTO ticketDTO);

    public List<TicketDTO> listTickets();

    public List<TicketDTO> listDispatchedTickets();

    public List<TicketDTO> listClosedTickets();

    public List<TicketDTO> listOpenTickets();

    public List<TicketDTO> findDispatchedTicketsByDate(Date date);

    public List<TicketDTO> findByUser(Long userId);

    public List<TicketDTO> findUnassignedTickets();

    public Optional<TicketDTO> getByTicketId(String ticketId);
}
