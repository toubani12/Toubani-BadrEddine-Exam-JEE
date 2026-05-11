import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-new-custmer',
  imports: [FormsModule, RouterLink],
  template: `
    <div class="container">
      <div class="card auth-card p-4">
        <h3 class="page-title text-center">Create account</h3>
        <form (submit)="$event.preventDefault(); onSubmit()">
          <div class="mb-3">
            <label class="form-label">First name</label>
            <input class="form-control" [(ngModel)]="firstName" name="firstName" required />
          </div>
          <div class="mb-3">
            <label class="form-label">Last name</label>
            <input class="form-control" [(ngModel)]="lastName" name="lastName" required />
          </div>
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input class="form-control" type="email" [(ngModel)]="email" name="email" required />
          </div>
          <div class="mb-3">
            <label class="form-label">Password</label>
            <input class="form-control" type="password" [(ngModel)]="password" name="password" required />
            <div class="form-text">You will be registered as ROLE_CLIENT.</div>
          </div>
          @if (error()) { <div class="alert alert-danger py-2">{{ error() }}</div> }
          <button class="btn btn-success w-100" type="submit" [disabled]="loading()">
            {{ loading() ? 'Creating account...' : 'Sign up' }}
          </button>
        </form>
        <hr />
        <p class="text-center mb-0 small">
          Already have an account? <a routerLink="/login">Sign in</a>
        </p>
      </div>
    </div>
  `,
})
export class NewCustmer {
  private auth = inject(AuthenticationService);
  private router = inject(Router);

  firstName = '';
  lastName = '';
  email = '';
  password = '';
  loading = signal(false);
  error = signal<string | null>(null);

  onSubmit(): void {
    this.error.set(null);
    this.loading.set(true);
    this.auth
      .register({ firstName: this.firstName, lastName: this.lastName, email: this.email, password: this.password })
      .subscribe({
        next: () => this.router.navigate(['/']),
        error: (err) => {
          this.error.set(err?.error?.message ?? 'Registration failed.');
          this.loading.set(false);
        },
        complete: () => this.loading.set(false),
      });
  }
}
