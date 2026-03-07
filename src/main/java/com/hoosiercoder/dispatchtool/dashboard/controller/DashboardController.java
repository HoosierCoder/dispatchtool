package com.hoosiercoder.dispatchtool.dashboard.controller;

import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.mapper.UserMapperImpl;
import com.hoosiercoder.dispatchtool.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Author: HoosierCoder
 *
 */
@Controller
public class DashboardController {

    private final UserService userService; // Inject your service here

    public DashboardController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/tenant/dashboard")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'ADMIN')")
    public String tenantDashboard(Model model, Principal principal) {
        // Fetch the DTO (The mapper now handles the tenant name)
        UserDTO userDto = userService.getByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("branchName", userDto.getTenantName());
        return "dashboard/tenant-admin";
    }

    @GetMapping("/system/dashboard")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public String systemDashboard(Model model, Principal principal) {
        // Fetch rdude's DTO - will contain "HoosierCoder Global HQ"
        UserDTO userDto = userService.getByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("System Admin not found"));

        model.addAttribute("branchName", userDto.getTenantName());
        return "dashboard/system-admin";
    }
}
