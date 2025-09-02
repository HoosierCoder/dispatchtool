package com.hoosiercoder.dispatchtool.counter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Author: HoosierCoder
 *
 */
@Entity
@Table(name = "ticket_counter")
public class TicketCounter {

    @Id
    private Long counterId;

    @NotNull
    @Column(nullable = false)
    private Long counter;

    public Long getCountId() {
        return counterId;
    }

    public void setCountId(Long counterId) {
        this.counterId = counterId;
    }

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }
}
