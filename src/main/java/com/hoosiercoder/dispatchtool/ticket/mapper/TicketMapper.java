package com.hoosiercoder.dispatchtool.ticket.mapper;

import com.hoosiercoder.dispatchtool.location.mapper.LocationMapper;
import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.user.mapper.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LocationMapper.class})
public interface TicketMapper {
    Ticket ticketDtoToTicket(TicketDTO ticketDTO);

    TicketDTO ticketToTicketDto(Ticket ticket);
}
