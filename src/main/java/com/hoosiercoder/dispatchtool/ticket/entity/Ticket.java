package com.hoosiercoder.dispatchtool.ticket.entity;

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

    private boolean isDispatched;

    private boolean isClosed;

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

    public Ticket(String ticketId, String summary, String description) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.isDispatched = false;
        this.isClosed = false;
    }

    public Ticket() {
        this.isDispatched = false;
        this.isClosed = false;
    }

    public Ticket(String ticketId, String summary, String description, User user) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.isDispatched = false;
        this.isClosed = false;
        this.user = user;
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

    public boolean isDispatched() {
        return isDispatched;
    }

    public void setDispatched(boolean dispatched) {
        isDispatched = dispatched;
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

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
