package com.hoosiercoder.dispatchtool.counter.repository;

import com.hoosiercoder.dispatchtool.counter.entity.TicketCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketCounterRepository extends JpaRepository<TicketCounter, Long> {

    TicketCounter findByCounterId(Long counterId);
}
