export const API_BASE_URL = 'http://localhost:8080/api';

export type Role = 'ROLE_ADMIN' | 'ROLE_EMPLOYE' | 'ROLE_CLIENT';

export interface AuthResponse {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
  email: string;
  firstName: string;
  lastName: string;
  roles: Role[];
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export type VehicleStatus = 'AVAILABLE' | 'RENTED' | 'IN_MAINTENANCE';
export type FuelType = 'GASOLINE' | 'DIESEL' | 'HYBRID' | 'ELECTRIC';
export type GearboxType = 'MANUAL' | 'AUTOMATIC';
export type MotorcycleType = 'SPORT' | 'SCOOTER' | 'ROADSTER' | 'TOURING';
export type RentalStatus = 'RESERVED' | 'ONGOING' | 'COMPLETED' | 'CANCELLED';

export interface Agency {
  id?: string;
  name: string;
  address: string;
  city: string;
  phone: string;
  vehiclesCount?: number;
}

export interface VehicleBase {
  id?: string;
  vehicleType?: 'CAR' | 'MOTORCYCLE';
  brand: string;
  model: string;
  registrationNumber: string;
  pricePerDay: number;
  serviceStartDate: string;
  status: VehicleStatus;
  agencyId?: string;
  agencyName?: string;
}

export interface Car extends VehicleBase {
  vehicleType?: 'CAR';
  numberOfDoors: number;
  fuelType: FuelType;
  gearboxType: GearboxType;
}

export interface Motorcycle extends VehicleBase {
  vehicleType?: 'MOTORCYCLE';
  engineDisplacementCc: number;
  motorcycleType: MotorcycleType;
  helmetIncluded: boolean;
}

export type Vehicle = Car | Motorcycle;

export interface Rental {
  id?: string;
  customerFullName: string;
  customerEmail: string;
  customerPhone: string;
  startDate: string;
  endDate: string;
  actualReturnDate?: string;
  totalPrice: number;
  status: RentalStatus;
  vehicleId: string;
  vehicleRegistrationNumber?: string;
  vehicleBrand?: string;
  vehicleModel?: string;
}

export interface RentalRequest {
  vehicleId: string;
  customerFullName: string;
  customerEmail: string;
  customerPhone: string;
  startDate: string;
  endDate: string;
}
