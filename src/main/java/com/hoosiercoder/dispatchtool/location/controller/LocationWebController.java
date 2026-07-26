package com.hoosiercoder.dispatchtool.location.controller;

import com.hoosiercoder.dispatchtool.location.dto.LocationDTO;
import com.hoosiercoder.dispatchtool.location.service.LocationService;
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
@RequestMapping("/tenant/{tenantId}/locations")
public class LocationWebController {

    private final LocationService locationService;

    public LocationWebController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public String listLocations(@PathVariable String tenantId, Model model) {
        List<LocationDTO> locations = locationService.listLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("tenantId", tenantId); // Pass tenantId to the view for links
        model.addAttribute("pageTitle", "Locations"); // Set page title for layout
        model.addAttribute("content", "location/list"); // Specify content fragment
        return "layout/main"; // Use the master layout
    }

    @GetMapping("/new")
    public String showCreateLocationForm(@PathVariable String tenantId, Model model) {
        model.addAttribute("location", new LocationDTO());
        model.addAttribute("tenantId", tenantId);
        model.addAttribute("isEdit", false); // Flag for form reuse
        model.addAttribute("pageTitle", "Create Location"); // Set page title for layout
        model.addAttribute("content", "location/form"); // Specify content fragment
        return "layout/main"; // Use the master layout
    }

    @PostMapping("/new")
    public String createLocation(@PathVariable String tenantId,
                                 @Valid @ModelAttribute("location") LocationDTO locationDto,
                                 BindingResult result,
                                 Model model, // Added Model to pass tenantId on error
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tenantId", tenantId); // Ensure tenantId is available on error
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Create Location"); // Set page title for layout
            model.addAttribute("content", "location/form"); // Specify content fragment
            return "layout/main"; // Use the master layout
        }
        locationService.createLocation(locationDto);
        redirectAttributes.addFlashAttribute("message", "Location created successfully!");
        return "redirect:/tenant/" + tenantId + "/locations";
    }

    @GetMapping("/{id}/edit")
    public String showEditLocationForm(@PathVariable String tenantId, @PathVariable Long id, Model model) {
        Optional<LocationDTO> location = locationService.getLocationById(id);
        if (location.isEmpty()) {
            // Handle not found, e.g., redirect to list with an error message
            return "redirect:/tenant/" + tenantId + "/locations";
        }
        model.addAttribute("location", location.get());
        model.addAttribute("tenantId", tenantId);
        model.addAttribute("isEdit", true); // Flag for form reuse
        model.addAttribute("pageTitle", "Edit Location"); // Set page title for layout
        model.addAttribute("content", "location/form"); // Specify content fragment
        return "layout/main"; // Use the master layout
    }

    @PostMapping("/{id}/edit")
    public String updateLocation(@PathVariable String tenantId, @PathVariable Long id,
                                 @Valid @ModelAttribute("location") LocationDTO locationDto,
                                 BindingResult result,
                                 Model model, // Added Model to pass tenantId on error
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("tenantId", tenantId); // Ensure tenantId is available on error
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Location"); // Set page title for layout
            model.addAttribute("content", "location/form"); // Specify content fragment
            return "layout/main"; // Use the master layout
        }
        try {
            locationService.updateLocation(id, locationDto);
            redirectAttributes.addFlashAttribute("message", "Location updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Location not found or could not be updated.");
        }
        return "redirect:/tenant/" + tenantId + "/locations";
    }

    @PostMapping("/{id}/delete")
    public String deleteLocation(@PathVariable String tenantId, @PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            locationService.deleteLocation(id);
            redirectAttributes.addFlashAttribute("message", "Location deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Location not found or could not be deleted.");
        }
        return "redirect:/tenant/" + tenantId + "/locations";
    }
}
