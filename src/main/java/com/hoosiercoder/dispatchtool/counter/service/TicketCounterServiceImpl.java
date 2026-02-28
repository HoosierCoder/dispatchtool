package com.hoosiercoder.dispatchtool.counter.service;

import com.hoosiercoder.dispatchtool.config.ConfigCache;
import com.hoosiercoder.dispatchtool.counter.entity.TicketCounter;
import com.hoosiercoder.dispatchtool.counter.repository.TicketCounterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: HoosierCoder
 *
 */
@Service
public class TicketCounterServiceImpl implements TicketCounterService {

    private static final String PREFIX = "TKT";

    @Autowired
    private TicketCounterRepository repository;

    @Override
    @Transactional // Ensures the increment is thread-safe
    public String getNextFormattedTicketNumber(String tenantId) {
        // 1. Fetch the specific counter for this tenant
        TicketCounter ticketCounter = repository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("No counter found for tenant: " + tenantId));

        // 2. Increment the value
        Long nextVal = ticketCounter.getCounter() + 1;
        ticketCounter.setCounter(nextVal);

        // 3. Save back to DB
        repository.save(ticketCounter);

        // 4. Return formatted string (e.g., TKT0000001)
        return String.format("%s%07d", PREFIX, nextVal.intValue());
    }
}
