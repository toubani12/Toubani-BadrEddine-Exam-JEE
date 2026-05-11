package toubani.badreddine.carloacation.entities;

import toubani.badreddine.carloacation.enums.FuelType;
import toubani.badreddine.carloacation.enums.GearboxType;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Car extends Vehicle {
	private int numberOfDoors;
	private FuelType fuelType;
	private GearboxType gearboxType;
}
