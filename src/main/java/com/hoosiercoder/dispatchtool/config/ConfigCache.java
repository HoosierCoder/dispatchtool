package com.hoosiercoder.dispatchtool.config;

import com.hoosiercoder.dispatchtool.counter.entity.TicketCounter;
import com.hoosiercoder.dispatchtool.counter.repository.TicketCounterRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: HoosierCoder
 *
 */
@Component
public class ConfigCache {

    // Key: tenantId, Value: the atomic counter for that company
    private final ConcurrentHashMap<String, AtomicLong> tenantCounters = new ConcurrentHashMap<>();

    @Autowired
    private TicketCounterRepository ticketCounterRepository;

    // We no longer use @PostConstruct to load EVERYTHING (to save RAM)
    // Instead, we load them "on demand"
    public Long getNextTicketNumber(String tenantId) {
        AtomicLong counter = tenantCounters.computeIfAbsent(tenantId, id -> {
            // If not in cache, fetch from DB or create a new one
            TicketCounter tc = ticketCounterRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tenant counter not initialized"));
            return new AtomicLong(tc.getCounter());
        });

        long nextVal = counter.incrementAndGet();

        // Strategy: Sync to DB immediately or periodically?
        // For a startup SaaS, syncing immediately is safer.
        updateDatabase(tenantId, nextVal);

        return nextVal;
    }

    @Async // Don't make the user wait for the DB write
    protected void updateDatabase(String tenantId, Long value) {
        // We'll need a specialized repo method to just update the count
        // to avoid loading the whole entity every time.
        ticketCounterRepository.updateCounter(tenantId, value);
    }
}