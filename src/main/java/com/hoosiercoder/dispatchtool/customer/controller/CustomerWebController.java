package com.hoosiercoder.dispatchtool.customer.controller;

import com.hoosiercoder.dispatchtool.customer.dto.CustomerDTO;
import com.hoosiercoder.dispatchtool.customer.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tenant/{tenantId}/customers")
public class CustomerWebController {

    private final CustomerService customerService;

    public CustomerWebController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(@PathVariable String tenantId, Model model) {
        List<CustomerDTO> customers = customerService.listCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("tenantId", tenantId); // Pass tenantId to the view for links
        return "customer/list"; // This will map to src/main/resources/templates/customer/list.html
    }

    @GetMapping("/new")
    public String showCreateCustomerForm(@PathVariable String tenantId, Model model) {
        model.addAttribute("customer", new CustomerDTO());
        model.addAttribute("tenantId", tenantId);
        model.addAttribute("isEdit", false); // Flag for form reuse
        return "customer/form"; // This will map to src/main/resources/templates/customer/form.html
    }

    @PostMapping("/new")
    public String createCustomer(@PathVariable String tenantId,
                                 @Valid @ModelAttribute("customer") CustomerDTO customerDto,
                                 BindingResult result,
                                 Model model, // Added Model to pass tenantId on error
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tenantId", tenantId); // Ensure tenantId is available on error
            model.addAttribute("isEdit", false);
            return "customer/form";
        }
        customerService.createCustomer(customerDto);
        redirectAttributes.addFlashAttribute("message", "Customer created successfully!");
        return "redirect:/tenant/" + tenantId + "/customers";
    }

    @GetMapping("/{id}/edit")
    public String showEditCustomerForm(@PathVariable String tenantId, @PathVariable Long id, Model model) {
        Optional<CustomerDTO> customer = customerService.getCustomerById(id);
        if (customer.isEmpty()) {
            // Handle not found, e.g., redirect to list with an error message
            return "redirect:/tenant/" + tenantId + "/customers";
        }
        model.addAttribute("customer", customer.get());
        model.addAttribute("tenantId", tenantId);
        model.addAttribute("isEdit", true); // Flag for form reuse
        return "customer/form";
    }

    @PostMapping("/{id}/edit")
    public String updateCustomer(@PathVariable String tenantId, @PathVariable Long id,
                                 @Valid @ModelAttribute("customer") CustomerDTO customerDto,
                                 BindingResult result,
                                 Model model, // Added Model to pass tenantId on error
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tenantId", tenantId); // Ensure tenantId is available on error
            model.addAttribute("isEdit", true);
            return "customer/form";
        }
        try {
            customerService.updateCustomer(id, customerDto);
            redirectAttributes.addFlashAttribute("message", "Customer updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Customer not found or could not be updated.");
        }
        return "redirect:/tenant/" + tenantId + "/customers";
    }

    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable String tenantId, @PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            customerService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("message", "Customer deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Customer not found or could not be deleted.");
        }
        return "redirect:/tenant/" + tenantId + "/customers";
    }
}
