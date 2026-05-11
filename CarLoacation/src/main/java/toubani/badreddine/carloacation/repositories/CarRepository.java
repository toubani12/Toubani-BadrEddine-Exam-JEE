package toubani.badreddine.carloacation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import toubani.badreddine.carloacation.entities.Car;

public interface CarRepository extends JpaRepository<Car, String> {
}
