package toubani.badreddine.carloacation.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.MotorcycleDTO;
import toubani.badreddine.carloacation.entities.Agency;
import toubani.badreddine.carloacation.entities.Motorcycle;
import toubani.badreddine.carloacation.enums.VehicleStatus;
import toubani.badreddine.carloacation.exceptions.BusinessException;
import toubani.badreddine.carloacation.exceptions.ResourceNotFoundException;
import toubani.badreddine.carloacation.mappers.VehicleMapper;
import toubani.badreddine.carloacation.repositories.AgencyRepository;
import toubani.badreddine.carloacation.repositories.MotorcycleRepository;
import toubani.badreddine.carloacation.repositories.VehicleRepository;
import toubani.badreddine.carloacation.services.MotorcycleService;

@Service
@Transactional
@RequiredArgsConstructor
public class MotorcycleServiceImpl implements MotorcycleService {

    private final MotorcycleRepository motorcycleRepository;
    private final AgencyRepository agencyRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public MotorcycleDTO createMotorcycle(MotorcycleDTO dto, String agencyId) {
        if (dto.getRegistrationNumber() != null
                && vehicleRepository.existsByRegistrationNumber(dto.getRegistrationNumber())) {
            throw new BusinessException("A vehicle with this registration number already exists: "
                    + dto.getRegistrationNumber());
        }
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Agency", agencyId));

        Motorcycle motorcycle = vehicleMapper.toMotorcycleEntity(dto);
        motorcycle.setAgency(agency);
        if (motorcycle.getStatus() == null) {
            motorcycle.setStatus(VehicleStatus.AVAILABLE);
        }
        return vehicleMapper.toMotorcycleDTO(motorcycleRepository.save(motorcycle));
    }

    @Override
    public MotorcycleDTO updateMotorcycle(String id, MotorcycleDTO dto) {
        Motorcycle motorcycle = motorcycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle", id));
        vehicleMapper.updateMotorcycleEntity(motorcycle, dto);
        return vehicleMapper.toMotorcycleDTO(motorcycleRepository.save(motorcycle));
    }

    @Override
    @Transactional(readOnly = true)
    public MotorcycleDTO getMotorcycleById(String id) {
        return motorcycleRepository.findById(id)
                .map(vehicleMapper::toMotorcycleDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Motorcycle", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MotorcycleDTO> getAllMotorcycles() {
        return motorcycleRepository.findAll().stream().map(vehicleMapper::toMotorcycleDTO).toList();
    }
}
