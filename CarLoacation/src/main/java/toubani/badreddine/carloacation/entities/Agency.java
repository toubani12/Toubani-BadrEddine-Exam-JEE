package toubani.badreddine.carloacation.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@ToString
public class Agency {
    @Id
    private String id = UUID.randomUUID().toString();

    private String name;
    private String address;
    private String city;
    private String phone;

    @OneToMany(mappedBy = "agency", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Vehicle> vehicles = new ArrayList<>();
}
