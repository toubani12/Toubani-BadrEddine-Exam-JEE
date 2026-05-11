package toubani.badreddine.carloacation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import toubani.badreddine.carloacation.entities.Motorcycle;

public interface MotorcycleRepository extends JpaRepository<Motorcycle, String> {
}
