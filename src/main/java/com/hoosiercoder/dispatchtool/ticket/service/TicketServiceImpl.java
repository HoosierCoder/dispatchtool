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
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author: HoosierCoder
 *
 */
@Service
public class TicketServiceImpl implements TicketService{

    private TicketRepository ticketRepository;
    private TicketMapper ticketMapper;
    private UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LocationRepository locationRepository;

    @Autowired
    private ConfigCache configCache;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
                             UserRepository userRepository, CustomerRepository customerRepository,
                             LocationRepository locationRepository) {
        this.ticketMapper = ticketMapper;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public TicketDTO saveNewTicket(TicketDTO ticketDTO) {
        String tenantId = TenantContext.getTenantId();
        Ticket ticket = ticketMapper.ticketDtoToTicket(ticketDTO);

        Long ticketNum = configCache.getNextTicketNumber(tenantId);
        ticket.setTicketId("TKT" + String.format("%07d", ticketNum.intValue()));

        ticket.setTenantId(tenantId);

        Ticket newTicket = ticketRepository.save(ticket);
        return ticketMapper.ticketToTicketDto(newTicket);
    }

    @Override
    public List<TicketDTO> listTickets() {
        String tenantId = TenantContext.getTenantId();
        return ticketRepository.findByTenantId(tenantId)
                .stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> findByUser(Long userId) {
        // We filter by BOTH to prevent cross-tenant data leaks
        String tenantId = TenantContext.getTenantId();
        return ticketRepository.findByTenantIdAndUser_UserId(tenantId,userId)
                .stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> findUnassignedTickets() {
        String tenantId = TenantContext.getTenantId();
        return ticketRepository.findByTenantIdAndUserIsNull(tenantId).stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketDTO> getByTicketId(String ticketId) {
        String tenantId = TenantContext.getTenantId();
        //return Optional.ofNullable(ticketMapper.ticketToTicketDto(ticketRepository.findById(ticketId)
        //        .orElse(null)));
        return Optional.ofNullable(ticketMapper.ticketToTicketDto(
                ticketRepository.findByTenantIdAndTicketId(tenantId, ticketId).orElse(null)));
    }

    @Override
    public List<TicketDTO> findTicketsByRange(java.time.Instant start, java.time.Instant end) {
        String tenantId = TenantContext.getTenantId();

        return ticketRepository.findByTenantIdAndCreatedDateBetween(tenantId, start, end)
                .stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> findTicketsByRangeAndStatus(java.time.Instant start, java.time.Instant end, TicketStatus status) {
        String tenantId = TenantContext.getTenantId();
        return ticketRepository.findByTenantIdAndStatusAndCreatedDateBetween(tenantId, status, start, end)
                .stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }
}
