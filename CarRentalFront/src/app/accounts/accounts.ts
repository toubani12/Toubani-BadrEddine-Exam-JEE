import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AgencyService } from '../services/agency.service';
import { AuthenticationService } from '../services/authentication.service';
import { Agency } from '../model/model';

@Component({
  selector: 'app-accounts',
  imports: [FormsModule],
  template: `
    <div class="container py-4">
      <h2 class="page-title">Agencies</h2>

      @if (auth.hasRole('ROLE_ADMIN')) {
        <div class="card p-3 mb-4">
          <h5>Add an agency</h5>
          <div class="row g-2">
            <div class="col-md-3"><input class="form-control" placeholder="Name" [(ngModel)]="draft.name" name="name" /></div>
            <div class="col-md-3"><input class="form-control" placeholder="Address" [(ngModel)]="draft.address" name="address" /></div>
            <div class="col-md-2"><input class="form-control" placeholder="City" [(ngModel)]="draft.city" name="city" /></div>
            <div class="col-md-2"><input class="form-control" placeholder="Phone" [(ngModel)]="draft.phone" name="phone" /></div>
            <div class="col-md-2"><button class="btn btn-primary w-100" (click)="create()">Create</button></div>
          </div>
          @if (error()) { <div class="alert alert-danger mt-2 mb-0 py-2">{{ error() }}</div> }
        </div>
      }

      <div class="card p-3">
        @if (loading()) {
          <p class="text-muted">Loading...</p>
        } @else if (agencies().length === 0) {
          <p class="text-muted mb-0">No agencies yet.</p>
        } @else {
          <table class="table table-hover align-middle mb-0">
            <thead>
              <tr><th>Name</th><th>City</th><th>Address</th><th>Phone</th><th>Vehicles</th>
              @if (auth.hasRole('ROLE_ADMIN')) { <th></th> }
              </tr>
            </thead>
            <tbody>
              @for (a of agencies(); track a.id) {
                <tr>
                  <td>{{ a.name }}</td>
                  <td>{{ a.city }}</td>
                  <td>{{ a.address }}</td>
                  <td>{{ a.phone }}</td>
                  <td>{{ a.vehiclesCount }}</td>
                  @if (auth.hasRole('ROLE_ADMIN')) {
                    <td><button class="btn btn-sm btn-outline-danger" (click)="remove(a)">Delete</button></td>
                  }
                </tr>
              }
            </tbody>
          </table>
        }
      </div>
    </div>
  `,
})
export class Accounts {
  private agencyService = inject(AgencyService);
  auth = inject(AuthenticationService);

  agencies = signal<Agency[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);
  draft: Agency = { name: '', address: '', city: '', phone: '' };

  constructor() {
    this.refresh();
  }

  refresh(): void {
    this.loading.set(true);
    this.agencyService.list().subscribe({
      next: (data) => { this.agencies.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  create(): void {
    this.error.set(null);
    this.agencyService.create(this.draft).subscribe({
      next: () => { this.draft = { name: '', address: '', city: '', phone: '' }; this.refresh(); },
      error: (err) => this.error.set(err?.error?.message ?? 'Could not create.'),
    });
  }

  remove(a: Agency): void {
    if (!a.id) return;
    if (!confirm(`Delete agency ${a.name}?`)) return;
    this.agencyService.delete(a.id).subscribe({
      next: () => this.refresh(),
      error: (err) => alert(err?.error?.message ?? 'Could not delete.'),
    });
  }
}
