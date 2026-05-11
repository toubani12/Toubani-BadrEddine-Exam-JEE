package toubani.badreddine.carloacation.mappers;

import org.springframework.stereotype.Component;

import toubani.badreddine.carloacation.dto.CarDTO;
import toubani.badreddine.carloacation.dto.MotorcycleDTO;
import toubani.badreddine.carloacation.dto.VehicleDTO;
import toubani.badreddine.carloacation.entities.Car;
import toubani.badreddine.carloacation.entities.Motorcycle;
import toubani.badreddine.carloacation.entities.Vehicle;

@Component
public class VehicleMapper {

    public VehicleDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) return null;
        if (vehicle instanceof Car car) {
            return toCarDTO(car);
        }
        if (vehicle instanceof Motorcycle motorcycle) {
            return toMotorcycleDTO(motorcycle);
        }
        throw new IllegalArgumentException("Unknown vehicle type: " + vehicle.getClass().getName());
    }

    public CarDTO toCarDTO(Car car) {
        if (car == null) return null;
        CarDTO dto = new CarDTO();
        fillCommonFields(dto, car);
        dto.setNumberOfDoors(car.getNumberOfDoors());
        dto.setFuelType(car.getFuelType());
        dto.setGearboxType(car.getGearboxType());
        return dto;
    }

    public MotorcycleDTO toMotorcycleDTO(Motorcycle motorcycle) {
        if (motorcycle == null) return null;
        MotorcycleDTO dto = new MotorcycleDTO();
        fillCommonFields(dto, motorcycle);
        dto.setEngineDisplacementCc(motorcycle.getEngineDisplacementCc());
        dto.setMotorcycleType(motorcycle.getMotorcycleType());
        dto.setHelmetIncluded(motorcycle.isHelmetIncluded());
        return dto;
    }

    public Car toCarEntity(CarDTO dto) {
        if (dto == null) return null;
        Car car = new Car();
        fillCommonFields(car, dto);
        car.setNumberOfDoors(dto.getNumberOfDoors());
        car.setFuelType(dto.getFuelType());
        car.setGearboxType(dto.getGearboxType());
        return car;
    }

    public Motorcycle toMotorcycleEntity(MotorcycleDTO dto) {
        if (dto == null) return null;
        Motorcycle motorcycle = new Motorcycle();
        fillCommonFields(motorcycle, dto);
        motorcycle.setEngineDisplacementCc(dto.getEngineDisplacementCc());
        motorcycle.setMotorcycleType(dto.getMotorcycleType());
        motorcycle.setHelmetIncluded(dto.isHelmetIncluded());
        return motorcycle;
    }

    public void updateCarEntity(Car target, CarDTO dto) {
        updateCommonFields(target, dto);
        if (dto.getNumberOfDoors() > 0) target.setNumberOfDoors(dto.getNumberOfDoors());
        if (dto.getFuelType() != null) target.setFuelType(dto.getFuelType());
        if (dto.getGearboxType() != null) target.setGearboxType(dto.getGearboxType());
    }

    public void updateMotorcycleEntity(Motorcycle target, MotorcycleDTO dto) {
        updateCommonFields(target, dto);
        if (dto.getEngineDisplacementCc() > 0) target.setEngineDisplacementCc(dto.getEngineDisplacementCc());
        if (dto.getMotorcycleType() != null) target.setMotorcycleType(dto.getMotorcycleType());
        target.setHelmetIncluded(dto.isHelmetIncluded());
    }

    private void fillCommonFields(VehicleDTO dto, Vehicle entity) {
        dto.setId(entity.getId());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setRegistrationNumber(entity.getRegistrationNumber());
        dto.setPricePerDay(entity.getPricePerDay());
        dto.setServiceStartDate(entity.getServiceStartDate());
        dto.setStatus(entity.getStatus());
        if (entity.getAgency() != null) {
            dto.setAgencyId(entity.getAgency().getId());
            dto.setAgencyName(entity.getAgency().getName());
        }
    }

    private void fillCommonFields(Vehicle entity, VehicleDTO dto) {
        if (dto.getId() != null && !dto.getId().isBlank()) {
            entity.setId(dto.getId());
        }
        entity.setBrand(dto.getBrand());
        entity.setModel(dto.getModel());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setPricePerDay(dto.getPricePerDay());
        entity.setServiceStartDate(dto.getServiceStartDate());
        entity.setStatus(dto.getStatus());
    }

    private void updateCommonFields(Vehicle target, VehicleDTO dto) {
        if (dto.getBrand() != null) target.setBrand(dto.getBrand());
        if (dto.getModel() != null) target.setModel(dto.getModel());
        if (dto.getRegistrationNumber() != null) target.setRegistrationNumber(dto.getRegistrationNumber());
        if (dto.getPricePerDay() > 0) target.setPricePerDay(dto.getPricePerDay());
        if (dto.getServiceStartDate() != null) target.setServiceStartDate(dto.getServiceStartDate());
        if (dto.getStatus() != null) target.setStatus(dto.getStatus());
    }
}
