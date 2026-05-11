package toubani.badreddine.carloacation.mappers;

import org.springframework.stereotype.Component;

import toubani.badreddine.carloacation.dto.RentalDTO;
import toubani.badreddine.carloacation.entities.Rental;
import toubani.badreddine.carloacation.entities.Vehicle;

@Component
public class RentalMapper {

    public RentalDTO toDTO(Rental rental) {
        if (rental == null) return null;
        RentalDTO.RentalDTOBuilder builder = RentalDTO.builder()
                .id(rental.getId())
                .customerFullName(rental.getCustomerFullName())
                .customerEmail(rental.getCustomerEmail())
                .customerPhone(rental.getCustomerPhone())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .actualReturnDate(rental.getActualReturnDate())
                .totalPrice(rental.getTotalPrice())
                .status(rental.getStatus());

        Vehicle vehicle = rental.getVehicle();
        if (vehicle != null) {
            builder.vehicleId(vehicle.getId())
                    .vehicleRegistrationNumber(vehicle.getRegistrationNumber())
                    .vehicleBrand(vehicle.getBrand())
                    .vehicleModel(vehicle.getModel());
        }
        return builder.build();
    }

    public Rental toEntity(RentalDTO dto) {
        if (dto == null) return null;
        Rental rental = new Rental();
        if (dto.getId() != null && !dto.getId().isBlank()) {
            rental.setId(dto.getId());
        }
        rental.setCustomerFullName(dto.getCustomerFullName());
        rental.setCustomerEmail(dto.getCustomerEmail());
        rental.setCustomerPhone(dto.getCustomerPhone());
        rental.setStartDate(dto.getStartDate());
        rental.setEndDate(dto.getEndDate());
        rental.setActualReturnDate(dto.getActualReturnDate());
        rental.setTotalPrice(dto.getTotalPrice());
        rental.setStatus(dto.getStatus());
        return rental;
    }
}
