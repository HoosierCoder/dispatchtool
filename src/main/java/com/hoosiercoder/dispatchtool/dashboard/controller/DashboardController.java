package com.hoosiercoder.dispatchtool.dashboard.controller;

import com.hoosiercoder.dispatchtool.dashboard.dto.DashboardDTO;
import com.hoosiercoder.dispatchtool.dashboard.service.DashboardService;
import com.hoosiercoder.dispatchtool.tenant.repository.TenantRepository;
import com.hoosiercoder.dispatchtool.user.dto.UserDTO;
import com.hoosiercoder.dispatchtool.user.entity.User;
import com.hoosiercoder.dispatchtool.user.repository.UserRepository;
import com.hoosiercoder.dispatchtool.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UserService userService;
    private final DashboardService dashboardService;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;

    @Autowired
    public DashboardController(UserService userService, DashboardService dashboardService, UserRepository userRepository, TenantRepository tenantRepository) {
        this.userService = userService;
        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
    }

    @GetMapping("/tenant/dashboard")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN', 'ADMIN', 'MANAGER', 'LEAD', 'ASSOCIATE')")
    public String tenantDashboard(Model model, Principal principal) {
        UserDTO userDto = userService.getByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String username = principal.getName();
        User userEntity;
        if (username.contains("@")) {
             String[] parts = username.split("@");
             userEntity = userRepository.findByTenantIdAndUsername(parts[1], parts[0]).orElseThrow();
        } else {
             userEntity = userRepository.findByUsername(username).orElseThrow();
        }

        DashboardDTO dashboardData = dashboardService.getDashboardData(userEntity);

        model.addAttribute("user", userDto);
        model.addAttribute("branchName", userDto.getTenantName());
        model.addAttribute("dashboard", dashboardData);

        return "dashboard/tenant-admin";
    }

    @GetMapping("/system/dashboard")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public String systemDashboard(Model model, Principal principal) {
        UserDTO userDto = userService.getByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("System Admin not found"));

        long totalTenants = tenantRepository.count();

        model.addAttribute("branchName", userDto.getTenantName());
        model.addAttribute("totalTenants", totalTenants);

        return "dashboard/system-admin";
    }
}
