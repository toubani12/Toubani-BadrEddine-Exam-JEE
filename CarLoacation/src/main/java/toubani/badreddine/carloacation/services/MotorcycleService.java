package toubani.badreddine.carloacation.services;

import java.util.List;

import toubani.badreddine.carloacation.dto.MotorcycleDTO;

public interface MotorcycleService {

    MotorcycleDTO createMotorcycle(MotorcycleDTO dto, String agencyId);

    MotorcycleDTO updateMotorcycle(String id, MotorcycleDTO dto);

    MotorcycleDTO getMotorcycleById(String id);

    List<MotorcycleDTO> getAllMotorcycles();
}
