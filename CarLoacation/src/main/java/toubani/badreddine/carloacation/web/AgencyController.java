package toubani.badreddine.carloacation.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.AgencyDTO;
import toubani.badreddine.carloacation.dto.VehicleDTO;
import toubani.badreddine.carloacation.services.AgencyService;

@RestController
@RequestMapping("/api/agencies")
@RequiredArgsConstructor
@Tag(name = "Agencies", description = "Manage rental agencies")
public class AgencyController {

    private final AgencyService agencyService;

    @Operation(summary = "Create a new agency")
    @PostMapping
    public ResponseEntity<AgencyDTO> create(@RequestBody AgencyDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agencyService.createAgency(dto));
    }

    @Operation(summary = "Update an existing agency")
    @PutMapping("/{id}")
    public AgencyDTO update(@PathVariable String id, @RequestBody AgencyDTO dto) {
        return agencyService.updateAgency(id, dto);
    }

    @Operation(summary = "Delete an agency (must have no vehicles attached)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        agencyService.deleteAgency(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get an agency by id")
    @GetMapping("/{id}")
    public AgencyDTO getById(@PathVariable String id) {
        return agencyService.getAgencyById(id);
    }

    @Operation(summary = "Get all agencies, optionally filtered by city or name")
    @GetMapping
    public List<AgencyDTO> getAll(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name) {
        if (city != null && !city.isBlank()) return agencyService.searchByCity(city);
        if (name != null && !name.isBlank()) return agencyService.searchByName(name);
        return agencyService.getAllAgencies();
    }

    @Operation(summary = "Get all vehicles belonging to a given agency")
    @GetMapping("/{id}/vehicles")
    public List<VehicleDTO> getVehicles(@PathVariable String id) {
        return agencyService.getVehiclesOfAgency(id);
    }
}
