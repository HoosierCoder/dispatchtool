package com.hoosiercoder.dispatchtool.ticket.mapper;

import com.hoosiercoder.dispatchtool.customer.mapper.CustomerMapper;
import com.hoosiercoder.dispatchtool.location.mapper.LocationMapper;
import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class,
        CustomerMapper.class,
        LocationMapper.class})
public interface TicketMapper {
    @Mapping(target = "tenantId", ignore = true) // Set by service layer
    @Mapping(target = "user", ignore = true) // Assigned by service layer
    @Mapping(target = "createdDate", ignore = true) // Handled by JPA/service
    @Mapping(target = "modifiedDate", ignore = true) // Handled by JPA/service
    Ticket ticketDtoToTicket(TicketDTO ticketDTO);

    @Mapping(source = "user.userId", target = "userId") // Map user entity's ID to DTO's userId
    TicketDTO ticketToTicketDto(Ticket ticket);
}
