package com.hoosiercoder.dispatchtool.ticket.entity;

import com.hoosiercoder.dispatchtool.customer.entity.Customer;
import com.hoosiercoder.dispatchtool.entity.BaseTenantEntity;
import com.hoosiercoder.dispatchtool.location.entity.Location;
import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;
import com.hoosiercoder.dispatchtool.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Date;

/**
 * Author: HoosierCoder
 *
 */
@Entity
@Table(name = "ticket")
public class Ticket extends BaseTenantEntity {

    @Id
    private String ticketId;

    private String summary;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Date dateDispatched;

    @CreationTimestamp
    @Column(name = "created_date")
    private Instant createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private Instant modifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.UNASSIGNED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Ticket(String ticketId, String summary, String description) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.status = TicketStatus.UNASSIGNED;
    }

    public Ticket() {
    }

    public Ticket(String ticketId, String summary, String description, User user,
                  Location location, Customer customer) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.user = user;
        this.location = location;
        this.customer = customer;
        this.status = (user != null) ? TicketStatus.ASSIGNED : TicketStatus.UNASSIGNED;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDateDispatched() {
        return dateDispatched;
    }

    public void setDateDispatched(Date dateDispatched) {
        this.dateDispatched = dateDispatched;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
