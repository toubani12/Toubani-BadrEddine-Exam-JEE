package toubani.badreddine.carloacation.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import toubani.badreddine.carloacation.entities.AppUser;
import toubani.badreddine.carloacation.enums.Role;
import toubani.badreddine.carloacation.repositories.UserRepository;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            seedUser(userRepository, passwordEncoder,
                    "Badr Eddine", "Toubani",
                    "fadre6@gmail.com", "B@dr1599...",
                    Set.of(Role.ROLE_ADMIN, Role.ROLE_EMPLOYE, Role.ROLE_CLIENT));

            seedUser(userRepository, passwordEncoder,
                    "Employee", "Demo",
                    "employee@demo.com", "Employee@123",
                    Set.of(Role.ROLE_EMPLOYE));

            seedUser(userRepository, passwordEncoder,
                    "Client", "Demo",
                    "client@demo.com", "Client@123",
                    Set.of(Role.ROLE_CLIENT));
        };
    }

    private void seedUser(UserRepository repo, PasswordEncoder encoder,
                          String firstName, String lastName,
                          String email, String rawPassword, Set<Role> roles) {
        if (repo.existsByEmail(email)) {
            log.info("User {} already present, skipping seed.", email);
            return;
        }
        AppUser user = AppUser.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(encoder.encode(rawPassword))
                .enabled(true)
                .roles(roles)
                .build();
        repo.save(user);
        log.info("Seeded user {} with roles {}", email, roles);
    }
}
