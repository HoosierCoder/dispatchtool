package com.hoosiercoder.dispatchtool.tenant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Author: HoosierCoder
 *
 */
public class TenantDTO {
    @NotBlank(message = "Tenant ID is required (usually a slug)")
    private String tenantId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Primary contact is required")
    private String primaryContactName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Primary email is required")
    private String primaryContactEmail;

    private String phoneNumber;
    private String accountStatus;
    private boolean active;
    private LocalDateTime createdAt;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPrimaryContactName() {
        return primaryContactName;
    }

    public void setPrimaryContactName(String primaryContactName) {
        this.primaryContactName = primaryContactName;
    }

    public String getPrimaryContactEmail() {
        return primaryContactEmail;
    }

    public void setPrimaryContactEmail(String primaryContactEmail) {
        this.primaryContactEmail = primaryContactEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
