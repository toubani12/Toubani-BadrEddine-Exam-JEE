package toubani.badreddine.carloacation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import toubani.badreddine.carloacation.enums.MotorcycleType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MotorcycleDTO extends VehicleDTO {
    private int engineDisplacementCc;
    private MotorcycleType motorcycleType;
    private boolean helmetIncluded;

    @Override
    public String getVehicleType() {
        return "MOTORCYCLE";
    }
}
