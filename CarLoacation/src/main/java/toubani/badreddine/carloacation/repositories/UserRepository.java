package toubani.badreddine.carloacation.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import toubani.badreddine.carloacation.entities.AppUser;

public interface UserRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
