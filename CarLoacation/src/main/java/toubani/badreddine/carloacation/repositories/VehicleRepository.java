package toubani.badreddine.carloacation.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import toubani.badreddine.carloacation.entities.Vehicle;
import toubani.badreddine.carloacation.enums.VehicleStatus;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByStatus(VehicleStatus status);

    List<Vehicle> findByAgencyId(String agencyId);

    List<Vehicle> findByBrandIgnoreCase(String brand);

    List<Vehicle> findByAgencyIdAndStatus(String agencyId, VehicleStatus status);

    boolean existsByRegistrationNumber(String registrationNumber);
}
