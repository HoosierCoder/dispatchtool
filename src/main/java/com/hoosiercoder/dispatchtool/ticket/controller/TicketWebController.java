package com.hoosiercoder.dispatchtool.ticket.controller;

import com.hoosiercoder.dispatchtool.context.TenantContext;
import com.hoosiercoder.dispatchtool.customer.service.CustomerService;
import com.hoosiercoder.dispatchtool.location.service.LocationService;
import com.hoosiercoder.dispatchtool.ticket.dto.TicketDTO;
import com.hoosiercoder.dispatchtool.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable; // Import PathVariable
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tenant/{tenantId}/tickets") // Updated RequestMapping
@PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'ADMIN', 'MANAGER', 'LEAD', 'ASSOCIATE')")
public class TicketWebController {

    private final TicketService ticketService;
    private final CustomerService customerService;
    private final LocationService locationService;

    @Autowired
    public TicketWebController(TicketService ticketService, CustomerService customerService, LocationService locationService) {
        this.ticketService = ticketService;
        this.customerService = customerService;
        this.locationService = locationService;
    }

    @GetMapping // Handles GET /tenant/{tenantId}/tickets
    public String listTickets(@PathVariable String tenantId, Model model) {
        List<TicketDTO> tickets = ticketService.listTickets(); // Assuming this lists tickets for the current tenant
        model.addAttribute("tickets", tickets);
        model.addAttribute("tenantId", tenantId);
        return "ticket/list"; // We'll create this template next
    }

    @GetMapping("/new")
    public String showCreateTicketForm(@PathVariable String tenantId, Model model) { // Added @PathVariable
        // DEBUG LOGGING
        System.out.println("DEBUG: TicketWebController.showCreateTicketForm");
        System.out.println("DEBUG: Current Tenant Context: " + TenantContext.getTenantId());
        
        var customers = customerService.findAllByTenant();
        var locations = locationService.findAllByTenant();
        
        System.out.println("DEBUG: Found " + customers.size() + " customers");
        System.out.println("DEBUG: Found " + locations.size() + " locations");

        model.addAttribute("ticket", new TicketDTO());
        model.addAttribute("customers", customers);
        model.addAttribute("locations", locations);
        model.addAttribute("tenantId", tenantId); // Pass tenantId to the form
        return "ticket/ticket-form";
    }

    @PostMapping("/new")
    public String createTicket(@PathVariable String tenantId, @ModelAttribute TicketDTO ticket) { // Added @PathVariable
        ticketService.saveNewTicket(ticket);
        return "redirect:/tenant/" + tenantId + "/dashboard"; // Updated redirect
    }
}
