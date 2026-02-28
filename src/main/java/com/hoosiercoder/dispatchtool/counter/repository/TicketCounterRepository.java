package com.hoosiercoder.dispatchtool.counter.repository;

import com.hoosiercoder.dispatchtool.counter.entity.TicketCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TicketCounterRepository extends JpaRepository<TicketCounter, String> {

    //TicketCounter findByCounterId(Long counterId);

    @Modifying
    @Query("UPDATE TicketCounter tc SET tc.counter = :count WHERE tc.tenantId = :tenantId")
    void updateCounter(String tenantId, Long count);
}
