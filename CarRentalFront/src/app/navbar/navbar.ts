import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark px-3">
      <a class="navbar-brand" routerLink="/">CarRental</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navMenu">
        @if (auth.isLoggedIn()) {
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/accounts" routerLinkActive="active">Agencies</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/customers" routerLinkActive="active">Vehicles</a>
            </li>
            @if (auth.hasAnyRole('ROLE_EMPLOYE','ROLE_ADMIN')) {
              <li class="nav-item">
                <a class="nav-link" routerLink="/rentals" routerLinkActive="active">Rentals</a>
              </li>
            }
          </ul>
          <ul class="navbar-nav ms-auto align-items-lg-center">
            <li class="nav-item me-3 text-light small">
              <span class="me-2">{{ auth.fullName() || auth.state()?.email }}</span>
              @for (r of auth.roles(); track r) {
                <span class="badge text-bg-info badge-role">{{ r.replace('ROLE_', '') }}</span>
              }
            </li>
            <li class="nav-item">
              <button class="btn btn-sm btn-outline-light" (click)="logout()">Logout</button>
            </li>
          </ul>
        } @else {
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/login">Login</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/new-custmer">Sign up</a>
            </li>
          </ul>
        }
      </div>
    </nav>
  `,
})
export class Navbar {
  auth = inject(AuthenticationService);
  private router = inject(Router);

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
