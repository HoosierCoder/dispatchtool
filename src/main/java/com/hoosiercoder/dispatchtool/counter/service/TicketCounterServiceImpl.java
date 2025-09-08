package com.hoosiercoder.dispatchtool.counter.service;

import com.hoosiercoder.dispatchtool.config.ConfigCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: HoosierCoder
 *
 */
@Service
public class TicketCounterServiceImpl implements TicketCounterService{

    private static final String PREFIX = "TKT";

    @Autowired
    protected ConfigCache configCache;

    @Override
    public String getNextFormattedTicketNumber() {
        Long tktNumber = configCache.getNextTicketNumber();

        return String.format("%s%07d", PREFIX, tktNumber.intValue());
    }
}
