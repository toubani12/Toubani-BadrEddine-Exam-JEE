package toubani.badreddine.carloacation.config;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import toubani.badreddine.carloacation.entities.Agency;
import toubani.badreddine.carloacation.entities.AppUser;
import toubani.badreddine.carloacation.entities.Car;
import toubani.badreddine.carloacation.entities.Motorcycle;
import toubani.badreddine.carloacation.entities.Rental;
import toubani.badreddine.carloacation.entities.Vehicle;
import toubani.badreddine.carloacation.enums.FuelType;
import toubani.badreddine.carloacation.enums.GearboxType;
import toubani.badreddine.carloacation.enums.MotorcycleType;
import toubani.badreddine.carloacation.enums.RentalStatus;
import toubani.badreddine.carloacation.enums.Role;
import toubani.badreddine.carloacation.enums.VehicleStatus;
import toubani.badreddine.carloacation.repositories.AgencyRepository;
import toubani.badreddine.carloacation.repositories.CarRepository;
import toubani.badreddine.carloacation.repositories.MotorcycleRepository;
import toubani.badreddine.carloacation.repositories.RentalRepository;
import toubani.badreddine.carloacation.repositories.UserRepository;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    @Order(1)
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

    @Bean
    @Order(2)
    public CommandLineRunner seedCatalog(AgencyRepository agencyRepo,
                                         CarRepository carRepo,
                                         MotorcycleRepository motoRepo,
                                         RentalRepository rentalRepo) {
        return args -> {
            if (agencyRepo.count() > 0) {
                log.info("Catalog already seeded, skipping.");
                return;
            }

            Agency casa = newAgency("Casablanca Center", "12 Bd Mohammed V", "Casablanca", "+212522000111");
            Agency rabat = newAgency("Rabat Agdal", "45 Av. de France", "Rabat", "+212537000222");
            Agency marrakech = newAgency("Marrakech Gueliz", "8 Rue de la Liberte", "Marrakech", "+212524000333");
            agencyRepo.saveAll(List.of(casa, rabat, marrakech));
            log.info("Seeded {} agencies.", 3);

            Car clio = newCar("Renault", "Clio V", "11-A-12345", 25.0, casa,
                    5, FuelType.GASOLINE, GearboxType.MANUAL);
            Car golf = newCar("Volkswagen", "Golf 8", "22-B-67890", 45.0, casa,
                    5, FuelType.DIESEL, GearboxType.AUTOMATIC);
            Car teslaModel3 = newCar("Tesla", "Model 3", "33-C-11111", 90.0, rabat,
                    4, FuelType.ELECTRIC, GearboxType.AUTOMATIC);
            Car dacia = newCar("Dacia", "Sandero", "44-D-22222", 18.0, marrakech,
                    5, FuelType.GASOLINE, GearboxType.MANUAL);
            carRepo.saveAll(List.of(clio, golf, teslaModel3, dacia));
            log.info("Seeded {} cars.", 4);

            Motorcycle ducati = newMoto("Ducati", "Panigale V4", "55-E-33333", 120.0, casa,
                    1100, MotorcycleType.SPORT, true);
            Motorcycle vespa = newMoto("Piaggio", "Vespa GTS 300", "66-F-44444", 35.0, rabat,
                    300, MotorcycleType.SCOOTER, true);
            Motorcycle bmwGs = newMoto("BMW", "R 1250 GS", "77-G-55555", 80.0, marrakech,
                    1254, MotorcycleType.TOURING, false);
            motoRepo.saveAll(List.of(ducati, vespa, bmwGs));
            log.info("Seeded {} motorcycles.", 3);

            LocalDate today = LocalDate.now();
            Rental r1 = newRental("Alice Bennani", "alice.b@example.com", "+212611223344",
                    today.plusDays(2), today.plusDays(5), clio, RentalStatus.RESERVED);
            Rental r2 = newRental("Karim Idrissi", "karim.i@example.com", "+212622334455",
                    today.minusDays(1), today.plusDays(3), golf, RentalStatus.ONGOING);
            golf.setStatus(VehicleStatus.RENTED);
            carRepo.save(golf);
            Rental r3 = newRental("Sofia Lahlou", "sofia.l@example.com", "+212633445566",
                    today.minusDays(10), today.minusDays(3), vespa, RentalStatus.COMPLETED);
            r3.setActualReturnDate(today.minusDays(3));
            Rental r4 = newRental("Yassine Amrani", "yassine.a@example.com", "+212644556677",
                    today.plusDays(1), today.plusDays(2), teslaModel3, RentalStatus.CANCELLED);
            rentalRepo.saveAll(List.of(r1, r2, r3, r4));
            log.info("Seeded {} rentals.", 4);
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

    private Agency newAgency(String name, String address, String city, String phone) {
        Agency a = new Agency();
        a.setName(name);
        a.setAddress(address);
        a.setCity(city);
        a.setPhone(phone);
        return a;
    }

    private Car newCar(String brand, String model, String reg, double price, Agency agency,
                       int doors, FuelType fuel, GearboxType gearbox) {
        Car c = new Car();
        fillVehicle(c, brand, model, reg, price, agency);
        c.setNumberOfDoors(doors);
        c.setFuelType(fuel);
        c.setGearboxType(gearbox);
        return c;
    }

    private Motorcycle newMoto(String brand, String model, String reg, double price, Agency agency,
                               int cc, MotorcycleType type, boolean helmet) {
        Motorcycle m = new Motorcycle();
        fillVehicle(m, brand, model, reg, price, agency);
        m.setEngineDisplacementCc(cc);
        m.setMotorcycleType(type);
        m.setHelmetIncluded(helmet);
        return m;
    }

    private void fillVehicle(Vehicle v, String brand, String model, String reg, double price, Agency agency) {
        v.setBrand(brand);
        v.setModel(model);
        v.setRegistrationNumber(reg);
        v.setPricePerDay(price);
        v.setServiceStartDate(LocalDate.now().minusYears(2));
        v.setStatus(VehicleStatus.AVAILABLE);
        v.setAgency(agency);
    }

    private Rental newRental(String fullName, String email, String phone,
                             LocalDate start, LocalDate end, Vehicle vehicle, RentalStatus status) {
        Rental r = new Rental();
        r.setCustomerFullName(fullName);
        r.setCustomerEmail(email);
        r.setCustomerPhone(phone);
        r.setStartDate(start);
        r.setEndDate(end);
        long days = Math.max(1, java.time.temporal.ChronoUnit.DAYS.between(start, end));
        r.setTotalPrice(days * vehicle.getPricePerDay());
        r.setStatus(status);
        r.setVehicle(vehicle);
        return r;
    }
}
