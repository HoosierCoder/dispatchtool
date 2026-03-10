package com.hoosiercoder.dispatchtool.dashboard.service;

import com.hoosiercoder.dispatchtool.dashboard.dto.DashboardDTO;
import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.service.TicketService;
import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.entity.UserRole;
import com.hoosiercoder.dispatchtool.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    public DashboardDTO getDashboardData(User user) {
        DashboardDTO dashboard = new DashboardDTO();

        // 1. Everyone sees their own tickets
        List<TicketDTO> myTickets = ticketService.findByUser(user.getUserId());
        dashboard.setMyTickets(myTickets);

        // 2. Everyone can see the team directory (for now)
        List<UserDTO> teamMembers = userService.listUsers();
        dashboard.setTeamMembers(teamMembers);

        // 3. Role-based data
        if (user.getUserRole() == UserRole.ASSOCIATE) {
            // Associates might see unassigned tickets to pick up
            dashboard.setUnassignedTickets(ticketService.findUnassignedTickets());
        } else if (user.getUserRole() == UserRole.LEAD || user.getUserRole() == UserRole.MANAGER || user.getUserRole() == UserRole.ADMIN) {
            // Leads/Managers see ALL open tickets
            // Note: You might need to add a method to TicketService for "findAllOpen"
            dashboard.setUnassignedTickets(ticketService.findUnassignedTickets());
            // dashboard.setAllOpenTickets(ticketService.findAllOpen()); // TODO: Implement this
        }

        if (user.getUserRole() == UserRole.MANAGER || user.getUserRole() == UserRole.ADMIN) {
            // Managers get stats
            Map<String, Long> stats = new HashMap<>();
            stats.put("totalOpen", (long) ticketService.listTickets().size()); // Simplified for now
            dashboard.setStats(stats);
        }

        return dashboard;
    }
}
