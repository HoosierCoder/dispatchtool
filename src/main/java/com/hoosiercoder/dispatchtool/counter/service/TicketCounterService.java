package com.hoosiercoder.dispatchtool.counter.service;

/**
 * Author: HoosierCoder
 *
 */
public interface TicketCounterService {
    // We now pass the tenantId to ensure we get the right sequence
    String getNextFormattedTicketNumber(String tenantId);
}
