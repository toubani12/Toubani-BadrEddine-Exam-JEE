package toubani.badreddine.carloacation.web;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.RentalDTO;
import toubani.badreddine.carloacation.dto.RentalRequestDTO;
import toubani.badreddine.carloacation.services.RentalService;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals", description = "Manage vehicle rentals and rental history")
public class RentalController {

    private final RentalService rentalService;

    @Operation(summary = "Create a new rental (status RESERVED). Checks vehicle availability for the requested period.")
    @PostMapping
    public ResponseEntity<RentalDTO> create(@RequestBody RentalRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.createRental(request));
    }

    @Operation(summary = "Start a rental: switches it to ONGOING and marks the vehicle as RENTED")
    @PostMapping("/{id}/start")
    public RentalDTO start(@PathVariable String id) {
        return rentalService.startRental(id);
    }

    @Operation(summary = "Complete a rental: records the actual return date and frees the vehicle")
    @PostMapping("/{id}/complete")
    public RentalDTO complete(
            @PathVariable String id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualReturnDate) {
        return rentalService.completeRental(id, actualReturnDate);
    }

    @Operation(summary = "Cancel a rental")
    @PostMapping("/{id}/cancel")
    public RentalDTO cancel(@PathVariable String id) {
        return rentalService.cancelRental(id);
    }

    @Operation(summary = "Get a rental by id")
    @GetMapping("/{id}")
    public RentalDTO getById(@PathVariable String id) {
        return rentalService.getRentalById(id);
    }

    @Operation(summary = "List rentals, optionally filtered by vehicle, customer email, or active state")
    @GetMapping
    public List<RentalDTO> getAll(
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        if (activeOnly) return rentalService.getActiveRentals();
        if (vehicleId != null && !vehicleId.isBlank()) return rentalService.getRentalsByVehicle(vehicleId);
        if (customerEmail != null && !customerEmail.isBlank()) return rentalService.getRentalsByCustomerEmail(customerEmail);
        return rentalService.getAllRentals();
    }

    @Operation(summary = "Check whether a vehicle is available for a given period")
    @GetMapping("/availability")
    public Map<String, Object> checkAvailability(
            @RequestParam String vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        boolean available = rentalService.isVehicleAvailable(vehicleId, startDate, endDate);
        return Map.of(
                "vehicleId", vehicleId,
                "startDate", startDate,
                "endDate", endDate,
                "available", available);
    }
}
