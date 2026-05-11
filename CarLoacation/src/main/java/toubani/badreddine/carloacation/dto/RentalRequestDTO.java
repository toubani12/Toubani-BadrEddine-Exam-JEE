package toubani.badreddine.carloacation.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RentalRequestDTO {
    private String vehicleId;
    private String customerFullName;
    private String customerEmail;
    private String customerPhone;
    private LocalDate startDate;
    private LocalDate endDate;
}
