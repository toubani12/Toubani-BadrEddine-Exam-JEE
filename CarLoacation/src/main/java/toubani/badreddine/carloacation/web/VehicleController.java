package toubani.badreddine.carloacation.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.VehicleDTO;
import toubani.badreddine.carloacation.enums.VehicleStatus;
import toubani.badreddine.carloacation.services.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles", description = "Generic operations on vehicles (cars and motorcycles)")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Get a vehicle by id")
    @GetMapping("/{id}")
    public VehicleDTO getById(@PathVariable String id) {
        return vehicleService.getVehicleById(id);
    }

    @Operation(summary = "Find a vehicle by its registration number")
    @GetMapping("/by-registration/{registrationNumber}")
    public VehicleDTO getByRegistrationNumber(@PathVariable String registrationNumber) {
        return vehicleService.getVehicleByRegistrationNumber(registrationNumber);
    }

    @Operation(summary = "List all vehicles with optional filters (status, agency, brand)")
    @GetMapping
    public List<VehicleDTO> getAll(
            @RequestParam(required = false) VehicleStatus status,
            @RequestParam(required = false) String agencyId,
            @RequestParam(required = false) String brand) {
        if (status != null) return vehicleService.getVehiclesByStatus(status);
        if (agencyId != null && !agencyId.isBlank()) return vehicleService.getVehiclesByAgency(agencyId);
        if (brand != null && !brand.isBlank()) return vehicleService.searchByBrand(brand);
        return vehicleService.getAllVehicles();
    }

    @Operation(summary = "List all available vehicles (optionally filtered by agency)")
    @GetMapping("/available")
    public List<VehicleDTO> getAvailable(@RequestParam(required = false) String agencyId) {
        if (agencyId != null && !agencyId.isBlank()) {
            return vehicleService.getAvailableVehiclesByAgency(agencyId);
        }
        return vehicleService.getAvailableVehicles();
    }

    @Operation(summary = "Update the status of a vehicle (AVAILABLE / RENTED / IN_MAINTENANCE)")
    @PatchMapping("/{id}/status")
    public VehicleDTO updateStatus(@PathVariable String id, @RequestParam VehicleStatus status) {
        return vehicleService.updateStatus(id, status);
    }

    @Operation(summary = "Assign a vehicle to a different agency")
    @PatchMapping("/{id}/agency/{agencyId}")
    public VehicleDTO assignAgency(@PathVariable String id, @PathVariable String agencyId) {
        return vehicleService.assignToAgency(id, agencyId);
    }

    @Operation(summary = "Delete a vehicle")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
