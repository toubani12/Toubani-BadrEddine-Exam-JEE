import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AuthenticationService } from '../services/authentication.service';
import { VehicleService } from '../services/vehicle.service';
import { RentalService } from '../services/rental.service';
import { Vehicle, VehicleStatus } from '../model/model';

@Component({
  selector: 'app-customers',
  imports: [FormsModule],
  template: `
    <div class="container py-4">
      <h2 class="page-title">Vehicles</h2>

      <div class="card p-3 mb-3">
        <div class="row g-2 align-items-center">
          <div class="col-md-2">
            <select class="form-select" [(ngModel)]="filterStatus" name="status" (change)="refresh()">
              <option value="">All statuses</option>
              <option value="AVAILABLE">AVAILABLE</option>
              <option value="RENTED">RENTED</option>
              <option value="IN_MAINTENANCE">IN_MAINTENANCE</option>
            </select>
          </div>
          <div class="col-md-3">
            <input class="form-control" placeholder="Filter by brand..." [(ngModel)]="filterBrand" name="brand" (keyup.enter)="refresh()" />
          </div>
          <div class="col-md-2">
            <button class="btn btn-outline-secondary w-100" (click)="refresh()">Apply</button>
          </div>
        </div>
      </div>

      <div class="card p-3">
        @if (loading()) { <p class="text-muted">Loading...</p> }
        @else if (vehicles().length === 0) { <p class="text-muted mb-0">No vehicles found.</p> }
        @else {
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr>
                <th>Type</th><th>Brand</th><th>Model</th><th>Registration</th>
                <th>Price/day</th><th>Status</th><th>Agency</th>
                @if (auth.hasAnyRole('ROLE_EMPLOYE','ROLE_ADMIN')) { <th>Set status</th> }
                @if (auth.isLoggedIn()) { <th></th> }
              </tr>
            </thead>
            <tbody>
              @for (v of vehicles(); track v.id) {
                <tr>
                  <td><span class="badge text-bg-secondary">{{ v.vehicleType }}</span></td>
                  <td>{{ v.brand }}</td>
                  <td>{{ v.model }}</td>
                  <td>{{ v.registrationNumber }}</td>
                  <td>{{ v.pricePerDay }} €</td>
                  <td><span class="badge"
                      [class.text-bg-success]="v.status==='AVAILABLE'"
                      [class.text-bg-warning]="v.status==='RENTED'"
                      [class.text-bg-danger]="v.status==='IN_MAINTENANCE'">{{ v.status }}</span></td>
                  <td>{{ v.agencyName }}</td>
                  @if (auth.hasAnyRole('ROLE_EMPLOYE','ROLE_ADMIN')) {
                    <td>
                      <select class="form-select form-select-sm" [ngModel]="v.status" name="setStatus-{{v.id}}" (ngModelChange)="changeStatus(v, $event)">
                        <option value="AVAILABLE">AVAILABLE</option>
                        <option value="RENTED">RENTED</option>
                        <option value="IN_MAINTENANCE">IN_MAINTENANCE</option>
                      </select>
                    </td>
                  }
                  @if (auth.isLoggedIn()) {
                    <td>
                      @if (v.status === 'AVAILABLE') {
                        <button class="btn btn-sm btn-success" (click)="startBooking(v)">Rent</button>
                      }
                    </td>
                  }
                </tr>
              }
            </tbody>
          </table>
        }
      </div>

      @if (bookingVehicle()) {
        <div class="card p-3 mt-3">
          <h5>Book "{{ bookingVehicle()?.brand }} {{ bookingVehicle()?.model }}"</h5>
          <div class="row g-2">
            <div class="col-md-3"><input class="form-control" placeholder="Full name" [(ngModel)]="booking.customerFullName" name="cFullName" /></div>
            <div class="col-md-3"><input class="form-control" placeholder="Email" [(ngModel)]="booking.customerEmail" name="cEmail" /></div>
            <div class="col-md-2"><input class="form-control" placeholder="Phone" [(ngModel)]="booking.customerPhone" name="cPhone" /></div>
            <div class="col-md-2"><input class="form-control" type="date" [(ngModel)]="booking.startDate" name="cStart" /></div>
            <div class="col-md-2"><input class="form-control" type="date" [(ngModel)]="booking.endDate" name="cEnd" /></div>
          </div>
          @if (bookingError()) { <div class="alert alert-danger py-2 mt-2 mb-0">{{ bookingError() }}</div> }
          @if (bookingSuccess()) { <div class="alert alert-success py-2 mt-2 mb-0">{{ bookingSuccess() }}</div> }
          <div class="mt-3">
            <button class="btn btn-primary me-2" (click)="confirmBooking()">Confirm booking</button>
            <button class="btn btn-outline-secondary" (click)="cancelBooking()">Cancel</button>
          </div>
        </div>
      }
    </div>
  `,
})
export class Customers {
  private vehicleService = inject(VehicleService);
  private rentalService = inject(RentalService);
  auth = inject(AuthenticationService);

  vehicles = signal<Vehicle[]>([]);
  loading = signal(true);
  filterStatus: VehicleStatus | '' = '';
  filterBrand = '';

  bookingVehicle = signal<Vehicle | null>(null);
  booking = { customerFullName: '', customerEmail: '', customerPhone: '', startDate: '', endDate: '' };
  bookingError = signal<string | null>(null);
  bookingSuccess = signal<string | null>(null);

  constructor() { this.refresh(); }

  refresh(): void {
    this.loading.set(true);
    this.vehicleService.list({
      status: this.filterStatus || undefined,
      brand: this.filterBrand || undefined,
    }).subscribe({
      next: (data) => { this.vehicles.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  changeStatus(v: Vehicle, status: VehicleStatus): void {
    if (!v.id) return;
    this.vehicleService.updateStatus(v.id, status).subscribe({
      next: () => this.refresh(),
      error: (err) => alert(err?.error?.message ?? 'Failed to update status.'),
    });
  }

  startBooking(v: Vehicle): void {
    this.bookingError.set(null);
    this.bookingSuccess.set(null);
    this.bookingVehicle.set(v);
    const email = this.auth.state()?.email ?? '';
    const fullName = this.auth.fullName();
    this.booking = { customerFullName: fullName, customerEmail: email, customerPhone: '', startDate: '', endDate: '' };
  }

  cancelBooking(): void { this.bookingVehicle.set(null); }

  confirmBooking(): void {
    const v = this.bookingVehicle();
    if (!v?.id) return;
    this.bookingError.set(null);
    this.rentalService.create({ vehicleId: v.id, ...this.booking }).subscribe({
      next: () => {
        this.bookingSuccess.set('Rental reserved!');
        this.bookingVehicle.set(null);
        this.refresh();
      },
      error: (err) => this.bookingError.set(err?.error?.message ?? 'Booking failed.'),
    });
  }
}
