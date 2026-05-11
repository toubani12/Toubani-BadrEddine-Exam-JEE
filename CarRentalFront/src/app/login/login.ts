import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  template: `
    <div class="container">
      <div class="card auth-card p-4">
        <h3 class="page-title text-center">Sign in</h3>
        <form (submit)="$event.preventDefault(); onSubmit()">
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input class="form-control" type="email" [(ngModel)]="email" name="email" required />
          </div>
          <div class="mb-3">
            <label class="form-label">Password</label>
            <input class="form-control" type="password" [(ngModel)]="password" name="password" required />
          </div>
          @if (error()) { <div class="alert alert-danger py-2">{{ error() }}</div> }
          <button class="btn btn-primary w-100" type="submit" [disabled]="loading()">
            {{ loading() ? 'Signing in...' : 'Sign in' }}
          </button>
        </form>
        <hr />
        <p class="text-center mb-0 small">
          No account? <a routerLink="/new-custmer">Create one</a>
        </p>
      </div>
    </div>
  `,
})
export class Login {
  private auth = inject(AuthenticationService);
  private router = inject(Router);

  email = '';
  password = '';
  loading = signal(false);
  error = signal<string | null>(null);

  onSubmit(): void {
    this.error.set(null);
    this.loading.set(true);
    this.auth.login({ email: this.email, password: this.password }).subscribe({
      next: () => this.router.navigate(['/']),
      error: (err) => {
        this.error.set(err?.error?.message ?? 'Login failed.');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }
}
