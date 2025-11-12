package com.hoosiercoder.dispatchtool.ticket.mapper;

import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket ticketDtoToTicket(TicketDTO ticketDTO);

    TicketDTO ticketToTicketDto(Ticket ticket);
}
