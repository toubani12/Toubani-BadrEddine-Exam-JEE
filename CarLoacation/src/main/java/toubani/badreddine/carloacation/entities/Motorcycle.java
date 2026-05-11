package toubani.badreddine.carloacation.entities;

import toubani.badreddine.carloacation.enums.MotorcycleType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Motorcycle extends Vehicle {
    private int engineDisplacementCc;

    @Enumerated(EnumType.STRING)
    private MotorcycleType motorcycleType;

    private boolean helmetIncluded;
}
