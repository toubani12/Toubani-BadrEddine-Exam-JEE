package toubani.badreddine.carloacation.services;

import java.util.List;

import toubani.badreddine.carloacation.dto.AgencyDTO;
import toubani.badreddine.carloacation.dto.VehicleDTO;

public interface AgencyService {

    AgencyDTO createAgency(AgencyDTO dto);

    AgencyDTO updateAgency(String id, AgencyDTO dto);

    void deleteAgency(String id);

    AgencyDTO getAgencyById(String id);

    List<AgencyDTO> getAllAgencies();

    List<AgencyDTO> searchByCity(String city);

    List<AgencyDTO> searchByName(String name);

    List<VehicleDTO> getVehiclesOfAgency(String agencyId);
}
