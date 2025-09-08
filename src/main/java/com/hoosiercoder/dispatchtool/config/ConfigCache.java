package com.hoosiercoder.dispatchtool.config;

import com.hoosiercoder.dispatchtool.counter.entity.TicketCounter;
import com.hoosiercoder.dispatchtool.counter.repository.TicketCounterRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: HoosierCoder
 *
 */
@Component
public class ConfigCache {
    private AtomicLong currentTicketNumber;

    @Autowired
    private TicketCounterRepository ticketCounterRepository;

    @PostConstruct
    public void initialization() {
        TicketCounter ticketCounter = ticketCounterRepository.findByCounterId(Long.valueOf(1));
        currentTicketNumber = new AtomicLong(ticketCounter.getCounter());
    }

    @PreDestroy
    public void preDestroy() {
        TicketCounter ticketCounter = ticketCounterRepository.findByCounterId(1L);
        ticketCounter.setCounter(currentTicketNumber.get());
        ticketCounterRepository.save(ticketCounter);
    }

    public Long getCurrentTicketNumber() {
        return currentTicketNumber.get();
    }

    public Long getNextTicketNumber() {
        return currentTicketNumber.incrementAndGet();
    }
}
