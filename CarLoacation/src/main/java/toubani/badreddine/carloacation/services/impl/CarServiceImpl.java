package toubani.badreddine.carloacation.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.CarDTO;
import toubani.badreddine.carloacation.entities.Agency;
import toubani.badreddine.carloacation.entities.Car;
import toubani.badreddine.carloacation.enums.VehicleStatus;
import toubani.badreddine.carloacation.exceptions.BusinessException;
import toubani.badreddine.carloacation.exceptions.ResourceNotFoundException;
import toubani.badreddine.carloacation.mappers.VehicleMapper;
import toubani.badreddine.carloacation.repositories.AgencyRepository;
import toubani.badreddine.carloacation.repositories.CarRepository;
import toubani.badreddine.carloacation.repositories.VehicleRepository;
import toubani.badreddine.carloacation.services.CarService;

@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final AgencyRepository agencyRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public CarDTO createCar(CarDTO dto, String agencyId) {
        if (dto.getRegistrationNumber() != null
                && vehicleRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
            throw new BusinessException("A vehicle with this registration number already exists: "
                    + dto.getRegistrationNumber());
        }
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Agency", agencyId));

        Car car = vehicleMapper.toCarEntity(dto);
        car.setAgency(agency);
        if (car.getStatus() == null) {
            car.setStatus(VehicleStatus.AVAILABLE);
        }
        return vehicleMapper.toCarDTO(carRepository.save(car));
    }

    @Override
    public CarDTO updateCar(String id, CarDTO dto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
        vehicleMapper.updateCarEntity(car, dto);
        return vehicleMapper.toCarDTO(carRepository.save(car));
    }

    @Override
    @Transactional(readOnly = true)
    public CarDTO getCarById(String id) {
        return carRepository.findById(id)
                .map(vehicleMapper::toCarDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream().map(vehicleMapper::toCarDTO).toList();
    }
}
