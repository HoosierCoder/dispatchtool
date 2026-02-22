package com.hoosiercoder.dispatchtool.ticket.enums;

public enum TicketStatus {
    UNASSIGNED("Unassigned"),
    ASSIGNED("Assigned"),
    DISPATCHED("Dispatched"),
    IN_PROGRESS("In Progress"),
    CLOSED("Closed"),
    CANCELLED("Cancelled");

    private final String displayName;

    // Constructor for the display name
    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isFinal() {
        return this == CLOSED || this == CANCELLED;
    }
}
