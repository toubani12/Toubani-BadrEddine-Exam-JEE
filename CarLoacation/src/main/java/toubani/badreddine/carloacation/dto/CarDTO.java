package toubani.badreddine.carloacation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import toubani.badreddine.carloacation.enums.FuelType;
import toubani.badreddine.carloacation.enums.GearboxType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CarDTO extends VehicleDTO {
    private int numberOfDoors;
    private FuelType fuelType;
    private GearboxType gearboxType;

    @Override
    public String getVehicleType() {
        return "CAR";
    }
}
