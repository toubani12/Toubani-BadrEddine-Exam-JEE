package toubani.badreddine.carloacation.dto;

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
public class AgencyDTO {
    private String id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private int vehiclesCount;
}
