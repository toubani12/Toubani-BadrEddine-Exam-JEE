package toubani.badreddine.carloacation.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import toubani.badreddine.carloacation.entities.Rental;
import toubani.badreddine.carloacation.enums.RentalStatus;

public interface RentalRepository extends JpaRepository<Rental, String> {

    List<Rental> findByVehicleId(String vehicleId);

    List<Rental> findByStatus(RentalStatus status);

    List<Rental> findByCustomerEmail(String customerEmail);

    @Query("SELECT r FROM Rental r WHERE r.vehicle.id = :vehicleId " +
            "AND r.status IN (toubani.badreddine.carloacation.enums.RentalStatus.RESERVED, " +
            "toubani.badreddine.carloacation.enums.RentalStatus.ONGOING) " +
            "AND r.startDate <= :endDate AND r.endDate >= :startDate")
    List<Rental> findOverlappingRentals(@Param("vehicleId") String vehicleId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);
}
