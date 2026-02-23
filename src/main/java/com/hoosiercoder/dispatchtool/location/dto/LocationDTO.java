package com.hoosiercoder.dispatchtool.location.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Author: HoosierCoder
 *
 */
public class LocationDTO {

    private Long id; // Included for updates/responses, usually null for new creations

    @NotBlank(message = "Street address is required")
    private String streetAddress;

    private String apartmentNumber;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 2, message = "Please use 2-letter state abbreviation")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Invalid Zip Code format")
    private String zipCode;

    public LocationDTO() {}

    public LocationDTO(Long id, String streetAddress, String apartmentNumber,
                       String city, String state, String zipCode) {
        this.id = id;
        this.streetAddress = streetAddress;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStreetAddress() { return streetAddress; }
    public void setStreetAddress(String streetAddress) { this.streetAddress = streetAddress; }

    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}
