package toubani.badreddine.carloacation.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.AgencyDTO;
import toubani.badreddine.carloacation.dto.VehicleDTO;
import toubani.badreddine.carloacation.entities.Agency;
import toubani.badreddine.carloacation.exceptions.BusinessException;
import toubani.badreddine.carloacation.exceptions.ResourceNotFoundException;
import toubani.badreddine.carloacation.mappers.AgencyMapper;
import toubani.badreddine.carloacation.mappers.VehicleMapper;
import toubani.badreddine.carloacation.repositories.AgencyRepository;
import toubani.badreddine.carloacation.services.AgencyService;

@Service
@Transactional
@RequiredArgsConstructor
public class AgencyServiceImpl implements AgencyService {

    private final AgencyRepository agencyRepository;
    private final AgencyMapper agencyMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    public AgencyDTO createAgency(AgencyDTO dto) {
        Agency agency = agencyMapper.toEntity(dto);
        Agency saved = agencyRepository.save(agency);
        return agencyMapper.toDTO(saved);
    }

    @Override
    public AgencyDTO updateAgency(String id, AgencyDTO dto) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agency", id));
        agencyMapper.updateEntity(agency, dto);
        return agencyMapper.toDTO(agencyRepository.save(agency));
    }

    @Override
    public void deleteAgency(String id) {
        Agency agency = agencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agency", id));
        if (agency.getVehicles() != null && !agency.getVehicles().isEmpty()) {
            throw new BusinessException("Cannot delete agency with assigned vehicles. Reassign or remove vehicles first.");
        }
        agencyRepository.delete(agency);
    }

    @Override
    @Transactional(readOnly = true)
    public AgencyDTO getAgencyById(String id) {
        return agencyRepository.findById(id)
                .map(agencyMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Agency", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgencyDTO> getAllAgencies() {
        return agencyRepository.findAll().stream().map(agencyMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgencyDTO> searchByCity(String city) {
        return agencyRepository.findByCityIgnoreCase(city).stream().map(agencyMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgencyDTO> searchByName(String name) {
        return agencyRepository.findByNameContainingIgnoreCase(name).stream().map(agencyMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesOfAgency(String agencyId) {
        Agency agency = agencyRepository.findById(agencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Agency", agencyId));
        return agency.getVehicles().stream().map(vehicleMapper::toDTO).toList();
    }
}
