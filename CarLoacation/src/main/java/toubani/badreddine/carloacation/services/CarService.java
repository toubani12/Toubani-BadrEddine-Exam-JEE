package toubani.badreddine.carloacation.services;

import java.util.List;

import toubani.badreddine.carloacation.dto.CarDTO;

public interface CarService {

    CarDTO createCar(CarDTO dto, String agencyId);

    CarDTO updateCar(String id, CarDTO dto);

    CarDTO getCarById(String id);

    List<CarDTO> getAllCars();
}
