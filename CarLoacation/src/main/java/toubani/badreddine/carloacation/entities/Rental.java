package toubani.badreddine.carloacation.entities;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import toubani.badreddine.carloacation.enums.RentalStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rental {
    @Id
    private String id = UUID.randomUUID().toString();

    private String customerFullName;
    private String customerEmail;
    private String customerPhone;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualReturnDate;

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @ManyToOne
    private Vehicle vehicle;
}
