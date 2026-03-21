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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tickets")
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

    @GetMapping("/new")
    public String showCreateTicketForm(Model model) {
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
        return "ticket/ticket-form";
    }

    @PostMapping("/new")
    public String createTicket(@ModelAttribute TicketDTO ticket) {
        ticketService.saveNewTicket(ticket);
        return "redirect:/tenant/dashboard";
    }
}
