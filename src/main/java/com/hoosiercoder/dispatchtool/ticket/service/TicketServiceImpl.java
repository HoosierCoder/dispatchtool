package com.hoosiercoder.dispatchtool.ticket.service;

import com.hoosiercoder.dispatchtool.config.ConfigCache;
import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
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

    @Autowired
    private ConfigCache configCache;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
                             UserRepository userRepository) {
        this.ticketMapper = ticketMapper;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TicketDTO saveNewTicket(TicketDTO ticketDTO) {
        Ticket ticket = ticketMapper.ticketDtoToTicket(ticketDTO);
        //any ticketId included in ticketDTO will be ignored
        //TicketId assigned here
        Long ticketNum = configCache.getNextTicketNumber();
        String ticketId = "TK" + String.format("%07d",ticketNum);
        ticket.setTicketId(ticketId);

        if (ticketDTO.getUserId() != null && ticketDTO.getUserId() > 0) {
            User user = Optional.ofNullable(userRepository.findById(ticketDTO.getUserId()))
                    .get().orElse(null);
            ticket.setUser(user);
        }

        Ticket newTicket = ticketRepository.save(ticket);
        return ticketMapper.ticketToTicketDto(newTicket);
    }

    @Override
    public List<TicketDTO> listTickets() {
        return ticketRepository.findAll()
                .stream().map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    /*@Override
    public List<TicketDTO> listDispatchedTickets() {
        return ticketRepository.findByIsDispatchedTrue().stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> listClosedTickets() {
        return ticketRepository.findByIsClosedTrue().stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }*/

    /*@Override
    public List<TicketDTO> listOpenTickets() {
        return ticketRepository.findByIsClosedFalse().stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }*/

    /*@Override
    public List<TicketDTO> findDispatchedTicketsByDate(Date date) {
        return ticketRepository.findByDateDispatched(date).stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }*/

    @Override
    public List<TicketDTO> findByUser(Long userId) {
        User user = userRepository.findByUserId(userId);

        return ticketRepository.findByUser(user).stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDTO> findUnassignedTickets() {
        return ticketRepository.findByUserIsNull().stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketDTO> getByTicketId(String ticketId) {
        return Optional.ofNullable(ticketMapper.ticketToTicketDto(ticketRepository.findById(ticketId)
                .orElse(null)));
    }
}
