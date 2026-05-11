package toubani.badreddine.carloacation.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.VehicleDTO;
import toubani.badreddine.carloacation.entities.Agency;
import toubani.badreddine.carloacation.entities.Vehicle;
import toubani.badreddine.carloacation.enums.VehicleStatus;
import toubani.badreddine.carloacation.exceptions.ResourceNotFoundException;
import toubani.badreddine.carloacation.mappers.VehicleMapper;
import toubani.badreddine.carloacation.repositories.AgencyRepository;
import toubani.badreddine.carloacation.repositories.VehicleRepository;
import toubani.badreddine.carloacation.services.VehicleService;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final AgencyRepository agencyRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(String id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber)
                .map(vehicleMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with registration number: " + registrationNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream().map(vehicleMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByStatus(VehicleStatus status) {
        return vehicleRepository.findByStatus(status).stream().map(vehicleMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByAgency(String agencyId) {
        return vehicleRepository.findByAgencyId(agencyId).stream().map(vehicleMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAvailableVehicles() {
        return vehicleRepository.findByStatus(VehicleStatus.AVAILABLE).stream()
                .map(vehicleMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAvailableVehiclesByAgency(String agencyId) {
        return vehicleRepository.findByAgencyIdAndStatus(agencyId, VehicleStatus.AVAILABLE).stream()
                .map(vehicleMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> searchByBrand(String brand) {
        return vehicleRepository.findByBrandIgnoreCase(brand).stream().map(vehicleMapper::toDTO).toList();
    }

    @Override
    public VehicleDTO updateStatus(String vehicleId, VehicleStatus status) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));
        vehicle.setStatus(status);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public VehicleDTO assignToAgency(String vehicleId, String agencyId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Agency", agencyId));
        vehicle.setAgency(agency);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public void deleteVehicle(String id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle", id);
        }
        vehicleRepository.deleteById(id);
    }
}
