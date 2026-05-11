package toubani.badreddine.carloacation.entities;

import java.time.LocalDate;
import java.util.UUID;

import toubani.badreddine.carloacation.enums.VehicleStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok. NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehicle {
    @Id
    private String id = UUID.randomUUID().toString();

    private String brand;
    private String model;
    private String registrationNumber;
    private double pricePerDay;
    private LocalDate serviceStartDate;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @ManyToOne
    private Agency agency;
}
