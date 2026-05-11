package toubani.badreddine.carloacation.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import toubani.badreddine.carloacation.enums.RentalStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RentalDTO {
    private String id;

    private String customerFullName;
    private String customerEmail;
    private String customerPhone;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;

    private double totalPrice;
    private RentalStatus status;

    private String vehicleId;
    private String vehicleRegistrationNumber;
    private String vehicleBrand;
    private String vehicleModel;
}
