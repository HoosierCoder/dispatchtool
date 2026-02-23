package com.hoosiercoder.dispatchtool.ticket.dto;

import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

/**
 * Author: HoosierCoder
 *
 */
public class TicketDTO {
    String ticketId;

    @NotBlank(message = "Summary cannot be empty")
    @Size(min = 5, max = 20, message = "Summary must be between 5 and 20 characters")
    private String summary;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 5, max = 500, message = "Description must be between 5 and 500 characters")
    private String description;

    private Long userId;

    private TicketStatus status;

    private Date dateDispatched;

    private CustomerDTO customer;
    private LocationDTO location;

    public TicketDTO(String ticketId, String summary, String description) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
    }

    public TicketDTO() {
        this.status = TicketStatus.UNASSIGNED;
    }

    public TicketDTO(String ticketId, String summary, String description, Long userId) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.userId = userId;
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

    public TicketStatus getStatus() {return this.status;}

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDateDispatched() {
        return dateDispatched;
    }

    public void setDateDispatched(Date dateDispatched) {
        this.dateDispatched = dateDispatched;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }
}
