package com.hoosiercoder.dispatchtool.ticket.entity;

import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;
import com.hoosiercoder.dispatchtool.user.entity.User;
import jakarta.persistence.*;
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
public class Ticket {

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

    public Ticket(String ticketId, String summary, String description) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.status = TicketStatus.UNASSIGNED;
    }

    public Ticket() {
    }

    public Ticket(String ticketId, String summary, String description, User user) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.user = user;
        this.status = TicketStatus.ASSIGNED;
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

}
