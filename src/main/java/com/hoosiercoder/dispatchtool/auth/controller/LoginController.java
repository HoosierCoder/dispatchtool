package com.hoosiercoder.dispatchtool.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Author: HoosierCoder
 *
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Standard login page (for system admins or generic login)
        return "login";
    }

    @GetMapping("/{tenantId}/login")
    public String tenantLogin(@PathVariable String tenantId, Model model) {
        // Tenant-specific login page
        model.addAttribute("tenantId", tenantId);
        return "login";
    }

    @GetMapping("/")
    public String index() {
        // Once logged in, redirect to a dashboard or home page
        return "index";
    }
}
