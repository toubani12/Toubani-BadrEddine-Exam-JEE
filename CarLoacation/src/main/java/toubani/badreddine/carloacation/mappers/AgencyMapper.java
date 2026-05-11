package toubani.badreddine.carloacation.mappers;

import org.springframework.stereotype.Component;

import toubani.badreddine.carloacation.dto.AgencyDTO;
import toubani.badreddine.carloacation.entities.Agency;

@Component
public class AgencyMapper {

    public AgencyDTO toDTO(Agency agency) {
        if (agency == null) return null;
        return AgencyDTO.builder()
                .id(agency.getId())
                .name(agency.getName())
                .address(agency.getAddress())
                .city(agency.getCity())
                .phone(agency.getPhone())
                .vehiclesCount(agency.getVehicles() != null ? agency.getVehicles().size() : 0)
                .build();
    }

    public Agency toEntity(AgencyDTO dto) {
        if (dto == null) return null;
        Agency agency = new Agency();
        if (dto.getId() != null && !dto.getId().isBlank()) {
            agency.setId(dto.getId());
        }
        agency.setName(dto.getName());
        agency.setAddress(dto.getAddress());
        agency.setCity(dto.getCity());
        agency.setPhone(dto.getPhone());
        return agency;
    }

    public void updateEntity(Agency target, AgencyDTO dto) {
        if (dto.getName() != null) target.setName(dto.getName());
        if (dto.getAddress() != null) target.setAddress(dto.getAddress());
        if (dto.getCity() != null) target.setCity(dto.getCity());
        if (dto.getPhone() != null) target.setPhone(dto.getPhone());
    }
}
