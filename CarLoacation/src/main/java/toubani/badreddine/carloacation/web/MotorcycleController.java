package toubani.badreddine.carloacation.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import toubani.badreddine.carloacation.dto.MotorcycleDTO;
import toubani.badreddine.carloacation.services.MotorcycleService;

@RestController
@RequestMapping("/api/motorcycles")
@RequiredArgsConstructor
@Tag(name = "Motorcycles", description = "Manage motorcycles")
public class MotorcycleController {

    private final MotorcycleService motorcycleService;

    @Operation(summary = "Register a new motorcycle under a given agency")
    @PostMapping
    public ResponseEntity<MotorcycleDTO> create(@RequestBody MotorcycleDTO dto, @RequestParam String agencyId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(motorcycleService.createMotorcycle(dto, agencyId));
    }

    @Operation(summary = "Update an existing motorcycle")
    @PutMapping("/{id}")
    public MotorcycleDTO update(@PathVariable String id, @RequestBody MotorcycleDTO dto) {
        return motorcycleService.updateMotorcycle(id, dto);
    }

    @Operation(summary = "Get a motorcycle by id")
    @GetMapping("/{id}")
    public MotorcycleDTO getById(@PathVariable String id) {
        return motorcycleService.getMotorcycleById(id);
    }

    @Operation(summary = "Get all motorcycles")
    @GetMapping
    public List<MotorcycleDTO> getAll() {
        return motorcycleService.getAllMotorcycles();
    }
}
