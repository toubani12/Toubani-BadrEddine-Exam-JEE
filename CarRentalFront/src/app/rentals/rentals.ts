import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { RentalService } from '../services/rental.service';
import { Rental } from '../model/model';

@Component({
  selector: 'app-rentals',
  imports: [FormsModule],
  template: `
    <div class="container py-4">
      <h2 class="page-title">Rentals</h2>

      <div class="card p-3 mb-3">
        <div class="form-check form-switch">
          <input class="form-check-input" type="checkbox" id="activeOnly" [(ngModel)]="activeOnly" name="activeOnly" (change)="refresh()" />
          <label class="form-check-label" for="activeOnly">Show only active (ONGOING)</label>
        </div>
      </div>

      <div class="card p-3">
        @if (loading()) { <p class="text-muted">Loading...</p> }
        @else if (rentals().length === 0) { <p class="text-muted mb-0">No rentals.</p> }
        @else {
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr>
                <th>Customer</th><th>Email</th><th>Vehicle</th><th>From</th><th>To</th>
                <th>Total</th><th>Status</th><th>Actions</th>
              </tr>
            </thead>
            <tbody>
              @for (r of rentals(); track r.id) {
                <tr>
                  <td>{{ r.customerFullName }}</td>
                  <td>{{ r.customerEmail }}</td>
                  <td>{{ r.vehicleBrand }} {{ r.vehicleModel }}<br/><small class="text-muted">{{ r.vehicleRegistrationNumber }}</small></td>
                  <td>{{ r.startDate }}</td>
                  <td>{{ r.endDate }}</td>
                  <td>{{ r.totalPrice }} €</td>
                  <td><span class="badge"
                    [class.text-bg-info]="r.status==='RESERVED'"
                    [class.text-bg-primary]="r.status==='ONGOING'"
                    [class.text-bg-success]="r.status==='COMPLETED'"
                    [class.text-bg-secondary]="r.status==='CANCELLED'">{{ r.status }}</span></td>
                  <td>
                    @if (r.status === 'RESERVED') {
                      <button class="btn btn-sm btn-primary me-1" (click)="start(r)">Start</button>
                      <button class="btn btn-sm btn-outline-danger" (click)="cancel(r)">Cancel</button>
                    }
                    @if (r.status === 'ONGOING') {
                      <button class="btn btn-sm btn-success" (click)="complete(r)">Complete</button>
                    }
                  </td>
                </tr>
              }
            </tbody>
          </table>
        }
      </div>
    </div>
  `,
})
export class Rentals {
  private rentalService = inject(RentalService);

  rentals = signal<Rental[]>([]);
  loading = signal(true);
  activeOnly = false;

  constructor() { this.refresh(); }

  refresh(): void {
    this.loading.set(true);
    this.rentalService.list({ activeOnly: this.activeOnly }).subscribe({
      next: (data) => { this.rentals.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  start(r: Rental): void {
    if (!r.id) return;
    this.rentalService.start(r.id).subscribe({ next: () => this.refresh(), error: (e) => alert(e?.error?.message ?? 'Failed.') });
  }

  complete(r: Rental): void {
    if (!r.id) return;
    this.rentalService.complete(r.id).subscribe({ next: () => this.refresh(), error: (e) => alert(e?.error?.message ?? 'Failed.') });
  }

  cancel(r: Rental): void {
    if (!r.id) return;
    this.rentalService.cancel(r.id).subscribe({ next: () => this.refresh(), error: (e) => alert(e?.error?.message ?? 'Failed.') });
  }
}
