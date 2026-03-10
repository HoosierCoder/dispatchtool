package com.hoosiercoder.dispatchtool.dashboard.dto;

import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.user.dto.UserDTO;

import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private List<TicketDTO> myTickets;
    private List<TicketDTO> unassignedTickets;
    private List<TicketDTO> allOpenTickets;
    private List<UserDTO> teamMembers;
    private Map<String, Long> stats;

    public List<TicketDTO> getMyTickets() {
        return myTickets;
    }

    public void setMyTickets(List<TicketDTO> myTickets) {
        this.myTickets = myTickets;
    }

    public List<TicketDTO> getUnassignedTickets() {
        return unassignedTickets;
    }

    public void setUnassignedTickets(List<TicketDTO> unassignedTickets) {
        this.unassignedTickets = unassignedTickets;
    }

    public List<TicketDTO> getAllOpenTickets() {
        return allOpenTickets;
    }

    public void setAllOpenTickets(List<TicketDTO> allOpenTickets) {
        this.allOpenTickets = allOpenTickets;
    }

    public List<UserDTO> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<UserDTO> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public Map<String, Long> getStats() {
        return stats;
    }

    public void setStats(Map<String, Long> stats) {
        this.stats = stats;
    }
}
