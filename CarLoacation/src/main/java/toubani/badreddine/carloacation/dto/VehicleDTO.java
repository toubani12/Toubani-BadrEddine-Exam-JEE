package toubani.badreddine.carloacation.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import toubani.badreddine.carloacation.enums.VehicleStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "vehicleType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CarDTO.class, name = "CAR"),
        @JsonSubTypes.Type(value = MotorcycleDTO.class, name = "MOTORCYCLE")
})
public abstract class VehicleDTO {
    private String id;
    private String brand;
    private String model;
    private String registrationNumber;
    private double pricePerDay;
    private LocalDate serviceStartDate;
    private VehicleStatus status;

    private String agencyId;
    private String agencyName;

    public abstract String getVehicleType();
}
