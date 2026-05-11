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
import toubani.badreddine.carloacation.dto.CarDTO;
import toubani.badreddine.carloacation.services.CarService;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Tag(name = "Cars", description = "Manage cars")
public class CarController {

    private final CarService carService;

    @Operation(summary = "Register a new car under a given agency")
    @PostMapping
    public ResponseEntity<CarDTO> create(@RequestBody CarDTO dto, @RequestParam String agencyId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar(dto, agencyId));
    }

    @Operation(summary = "Update an existing car")
    @PutMapping("/{id}")
    public CarDTO update(@PathVariable String id, @RequestBody CarDTO dto) {
        return carService.updateCar(id, dto);
    }

    @Operation(summary = "Get a car by id")
    @GetMapping("/{id}")
    public CarDTO getById(@PathVariable String id) {
        return carService.getCarById(id);
    }

    @Operation(summary = "Get all cars")
    @GetMapping
    public List<CarDTO> getAll() {
        return carService.getAllCars();
    }
}
