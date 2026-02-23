package com.hoosiercoder.dispatchtool.customer.dto;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * Author: HoosierCoder
 *
 */
public class CustomerDTO {

    private Long id;

    private String firstName;

    @NotBlank(message = "Last name or Company name is required")
    private String lastName;

    @Email(message = "Please provide a valid email address")
    private String email;

    private String phoneNumber;

    // This nests the Location data right inside the Customer response
    private LocationDTO billingAddress;

    // Optional: Only include this if you want to see a list of tickets
    // when you view a customer's profile
    // private List<TicketDTO> tickets;

    // Standard Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocationDTO getBillingAddress() { return billingAddress; }
    public void setBillingAddress(LocationDTO billingAddress) { this.billingAddress = billingAddress; }
}
