package toubani.badreddine.carloacation.services.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import toubani.badreddine.carloacation.dto.RentalDTO;
import toubani.badreddine.carloacation.dto.RentalRequestDTO;
import toubani.badreddine.carloacation.entities.Rental;
import toubani.badreddine.carloacation.entities.Vehicle;
import toubani.badreddine.carloacation.enums.RentalStatus;
import toubani.badreddine.carloacation.enums.VehicleStatus;
import toubani.badreddine.carloacation.exceptions.BusinessException;
import toubani.badreddine.carloacation.exceptions.ResourceNotFoundException;
import toubani.badreddine.carloacation.mappers.RentalMapper;
import toubani.badreddine.carloacation.repositories.RentalRepository;
import toubani.badreddine.carloacation.repositories.VehicleRepository;
import toubani.badreddine.carloacation.services.RentalService;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalMapper rentalMapper;

    @Override
    public RentalDTO createRental(RentalRequestDTO request) {
        validateDates(request.getStartDate(), request.getEndDate());

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", request.getVehicleId()));

        if (vehicle.getStatus() == VehicleStatus.IN_MAINTENANCE) {
            throw new BusinessException("Vehicle is currently in maintenance and cannot be rented.");
        }

        List<Rental> overlapping = rentalRepository.findOverlappingRentals(
                vehicle.getId(), request.getStartDate(), request.getEndDate());
        if (!overlapping.isEmpty()) {
            throw new BusinessException(
                    "Vehicle is already booked for the requested period.");
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) days = 1;
        double totalPrice = days * vehicle.getPricePerDay();

        Rental rental = new Rental();
        rental.setCustomerFullName(request.getCustomerFullName());
        rental.setCustomerEmail(request.getCustomerEmail());
        rental.setCustomerPhone(request.getCustomerPhone());
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rental.setTotalPrice(totalPrice);
        rental.setStatus(RentalStatus.RESERVED);
        rental.setVehicle(vehicle);

        return rentalMapper.toDTO(rentalRepository.save(rental));
    }

    @Override
    public RentalDTO startRental(String rentalId) {
        Rental rental = findRentalOrThrow(rentalId);
        if (rental.getStatus() != RentalStatus.RESERVED) {
            throw new BusinessException("Only reserved rentals can be started. Current status: " + rental.getStatus());
        }
        rental.setStatus(RentalStatus.ONGOING);
        rental.getVehicle().setStatus(VehicleStatus.RENTED);
        vehicleRepository.save(rental.getVehicle());
        return rentalMapper.toDTO(rentalRepository.save(rental));
    }

    @Override
    public RentalDTO completeRental(String rentalId, LocalDate actualReturnDate) {
        Rental rental = findRentalOrThrow(rentalId);
        if (rental.getStatus() != RentalStatus.ONGOING && rental.getStatus() != RentalStatus.RESERVED) {
            throw new BusinessException("Cannot complete rental in status: " + rental.getStatus());
        }
        LocalDate effectiveReturn = actualReturnDate != null ? actualReturnDate : LocalDate.now();
        rental.setActualReturnDate(effectiveReturn);
        rental.setStatus(RentalStatus.COMPLETED);

        if (effectiveReturn.isAfter(rental.getEndDate())) {
            long extraDays = ChronoUnit.DAYS.between(rental.getEndDate(), effectiveReturn);
            rental.setTotalPrice(rental.getTotalPrice() + extraDays * rental.getVehicle().getPricePerDay());
        }

        rental.getVehicle().setStatus(VehicleStatus.AVAILABLE);
        vehicleRepository.save(rental.getVehicle());
        return rentalMapper.toDTO(rentalRepository.save(rental));
    }

    @Override
    public RentalDTO cancelRental(String rentalId) {
        Rental rental = findRentalOrThrow(rentalId);
        if (rental.getStatus() == RentalStatus.COMPLETED || rental.getStatus() == RentalStatus.CANCELLED) {
            throw new BusinessException("Rental already finalized: " + rental.getStatus());
        }
        rental.setStatus(RentalStatus.CANCELLED);
        if (rental.getVehicle().getStatus() == VehicleStatus.RENTED) {
            rental.getVehicle().setStatus(VehicleStatus.AVAILABLE);
            vehicleRepository.save(rental.getVehicle());
        }
        return rentalMapper.toDTO(rentalRepository.save(rental));
    }

    @Override
    @Transactional(readOnly = true)
    public RentalDTO getRentalById(String id) {
        return rentalMapper.toDTO(findRentalOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream().map(rentalMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getRentalsByVehicle(String vehicleId) {
        return rentalRepository.findByVehicleId(vehicleId).stream().map(rentalMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getActiveRentals() {
        return rentalRepository.findByStatus(RentalStatus.ONGOING).stream()
                .map(rentalMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getRentalsByCustomerEmail(String email) {
        return rentalRepository.findByCustomerEmail(email).stream().map(rentalMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isVehicleAvailable(String vehicleId, LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", vehicleId));
        if (vehicle.getStatus() == VehicleStatus.IN_MAINTENANCE) return false;
        return rentalRepository.findOverlappingRentals(vehicleId, startDate, endDate).isEmpty();
    }

    private Rental findRentalOrThrow(String id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", id));
    }

    private void validateDates(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new BusinessException("Start date and end date are required.");
        }
        if (end.isBefore(start)) {
            throw new BusinessException("End date must be after start date.");
        }
    }
}
