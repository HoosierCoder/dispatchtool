package com.hoosiercoder.dispatchtool.tenant.controller;

import com.hoosiercoder.dispatchtool.tenant.dto.TenantDTO;
import com.hoosiercoder.dispatchtool.tenant.entity.Tenant;
import com.hoosiercoder.dispatchtool.tenant.mapper.TenantMapper;
import com.hoosiercoder.dispatchtool.tenant.repository.TenantRepository;
import com.hoosiercoder.dispatchtool.tenant.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/system/tenants")
@PreAuthorize("hasRole('SYSTEM_ADMIN')")
public class TenantController {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    private final TenantService tenantService;
    private final TenantMapper tenantMapper;
    private final TenantRepository tenantRepository;

    @Autowired
    public TenantController(TenantService tenantService, TenantMapper tenantMapper, TenantRepository tenantRepository) {
        this.tenantService = tenantService;
        this.tenantMapper = tenantMapper;
        this.tenantRepository = tenantRepository;
    }

    @GetMapping
    public String listTenants(Model model) {
        List<TenantDTO> tenants = tenantService.listAllTenants();
        model.addAttribute("tenants", tenants);
        return "system/tenant-list";
    }

    @GetMapping("/{id}")
    public String viewTenant(@PathVariable String id, Model model) {
        TenantDTO tenantDto = tenantService.getTenantById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tenant Id:" + id));
        model.addAttribute("tenant", tenantDto);
        return "system/tenant-details";
    }

    @GetMapping("/new")
    public String showCreateTenantForm(Model model) {
        model.addAttribute("tenant", new Tenant());
        return "system/tenant-form";
    }

    @PostMapping("/new")
    public String createTenant(@ModelAttribute Tenant tenant) {
        String tenantId = generateTenantId(tenant.getCompanyName());
        tenant.setTenantId(tenantId);

        TenantDTO tenantDto = tenantMapper.tenantToTenantDto(tenant);
        
        tenantService.createTenant(tenantDto);
        return "redirect:/system/tenants";
    }

    @GetMapping("/edit/{id}")
    public String showEditTenantForm(@PathVariable String id, Model model) {
        TenantDTO tenantDto = tenantService.getTenantById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tenant Id:" + id));
        model.addAttribute("tenant", tenantDto);
        return "system/tenant-form";
    }

    @PostMapping("/edit/{id}")
    public String updateTenant(@PathVariable String id, @ModelAttribute TenantDTO tenantDto) {
        tenantDto.setTenantId(id);
        tenantService.updateTenant(tenantDto);
        return "redirect:/system/tenants/" + id;
    }

    @PostMapping("/toggle-status/{id}")
    public String toggleTenantStatus(@PathVariable String id) {
        tenantService.toggleTenantStatus(id);
        return "redirect:/system/tenants/" + id;
    }

    private String generateTenantId(String companyName) {
        String nowhitespace = WHITESPACE.matcher(companyName).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = slug.toLowerCase(Locale.ENGLISH);

        int counter = 0;
        String finalSlug = slug;
        while (tenantRepository.existsById(finalSlug)) {
            counter++;
            finalSlug = slug + "-" + counter;
        }
        return finalSlug;
    }
}
