package com.hoosiercoder.dispatchtool.ticket.service;

import com.hoosiercoder.dispatchtool.ticket.enums.TicketStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketWorkflowTest {

    @Test
    @DisplayName("CLOSED and CANCELLED statuses should be marked as final")
    public void whenStatusIsClosedOrCancelled_shouldBeFinal() {
        assertThat(TicketStatus.CLOSED.isFinal()).isTrue();
        assertThat(TicketStatus.CANCELLED.isFinal()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = TicketStatus.class, names = {"UNASSIGNED", "ASSIGNED", "DISPATCHED", "IN_PROGRESS", "RESOLVED"})
    @DisplayName("Active statuses should not be marked as final")
    public void whenStatusIsActive_shouldNotBeFinal(TicketStatus status) {
        assertThat(status.isFinal()).isFalse();
    }

    @Test
    @DisplayName("Verify status human-readable display names")
    public void verifyDisplayNames() {
        assertThat(TicketStatus.UNASSIGNED.getDisplayName()).isEqualTo("Unassigned");
        assertThat(TicketStatus.IN_PROGRESS.getDisplayName()).isEqualTo("In Progress");
        assertThat(TicketStatus.DISPATCHED.getDisplayName()).isEqualTo("Dispatched");
    }
}
