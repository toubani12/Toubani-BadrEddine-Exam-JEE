package toubani.badreddine.carloacation.services;

import java.time.LocalDate;
import java.util.List;

import toubani.badreddine.carloacation.dto.RentalDTO;
import toubani.badreddine.carloacation.dto.RentalRequestDTO;

public interface RentalService {

    RentalDTO createRental(RentalRequestDTO request);

    RentalDTO startRental(String rentalId);

    RentalDTO completeRental(String rentalId, LocalDate actualReturnDate);

    RentalDTO cancelRental(String rentalId);

    RentalDTO getRentalById(String id);

    List<RentalDTO> getAllRentals();

    List<RentalDTO> getRentalsByVehicle(String vehicleId);

    List<RentalDTO> getActiveRentals();

    List<RentalDTO> getRentalsByCustomerEmail(String email);

    boolean isVehicleAvailable(String vehicleId, LocalDate startDate, LocalDate endDate);
}
