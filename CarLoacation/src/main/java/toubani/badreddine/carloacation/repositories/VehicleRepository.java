package toubani.badreddine.carloacation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import toubani.badreddine.carloacation.entities.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
