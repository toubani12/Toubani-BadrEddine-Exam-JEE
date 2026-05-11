package toubani.badreddine.carloacation.services;

import java.util.List;

import toubani.badreddine.carloacation.dto.VehicleDTO;
import toubani.badreddine.carloacation.enums.VehicleStatus;

public interface VehicleService {

    VehicleDTO getVehicleById(String id);

    VehicleDTO getVehicleByRegistrationNumber(String registrationNumber);

    List<VehicleDTO> getAllVehicles();

    List<VehicleDTO> getVehiclesByStatus(VehicleStatus status);

    List<VehicleDTO> getVehiclesByAgency(String agencyId);

    List<VehicleDTO> getAvailableVehicles();

    List<VehicleDTO> getAvailableVehiclesByAgency(String agencyId);

    List<VehicleDTO> searchByBrand(String brand);

    VehicleDTO updateStatus(String vehicleId, VehicleStatus status);

    VehicleDTO assignToAgency(String vehicleId, String agencyId);

    void deleteVehicle(String id);
}
