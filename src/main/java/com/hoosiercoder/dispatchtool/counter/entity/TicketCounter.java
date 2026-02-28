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
    @Column(name = "tenant_id", length = 50)
    private String tenantId;

    @NotNull
    @Column(nullable = false)
    private Long counter;

    // Getters and Setters
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getCounter() { return counter; }
    public void setCounter(Long counter) { this.counter = counter; }
}
