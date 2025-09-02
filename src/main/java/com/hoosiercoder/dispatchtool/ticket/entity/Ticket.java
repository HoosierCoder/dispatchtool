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

}
