package com.hoosiercoder.dispatchtool.ticket.dto;

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

    private boolean isDispatched;

    private Long userId;

    private Date dateDispatched;

    public TicketDTO(String ticketId, String summary, String description) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.isDispatched = false;
    }

    public TicketDTO() {
        this.setDispatched(false);
    }

    public TicketDTO(String ticketId, String summary, String description, Long userId) {
        this.ticketId = ticketId;
        this.summary = summary;
        this.description = description;
        this.userId = userId;
        this.isDispatched = false;
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
}
