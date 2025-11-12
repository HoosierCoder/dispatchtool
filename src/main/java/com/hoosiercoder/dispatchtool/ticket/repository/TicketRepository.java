package com.hoosiercoder.dispatchtool.ticket.repository;

import com.hoosiercoder.dispatchtool.ticket.entity.Ticket;
import com.hoosiercoder.dispatchtool.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TicketRepository  extends JpaRepository<Ticket, String> {
    public List<Ticket> findByIsDispatchedTrue();

    public List<Ticket> findByIsDispatchedFalse();

    public List<Ticket> findByIsClosedTrue();

    public List<Ticket> findByIsClosedFalse();

    public List<Ticket> findByUser(User user);

    public List<Ticket> findByDateDispatched(Date dispatchDate);

    public List<Ticket> findByUserIsNull();
}
