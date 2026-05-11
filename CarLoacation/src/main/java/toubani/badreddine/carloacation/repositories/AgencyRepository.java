package toubani.badreddine.carloacation.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import toubani.badreddine.carloacation.entities.Agency;

public interface AgencyRepository extends JpaRepository<Agency, String> {

    List<Agency> findByCityIgnoreCase(String city);

    List<Agency> findByNameContainingIgnoreCase(String name);
}
