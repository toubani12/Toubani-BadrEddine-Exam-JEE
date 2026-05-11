import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-not-authorised',
  imports: [RouterLink],
  template: `
    <div class="container py-5 text-center">
      <h1 class="display-3 text-danger">403</h1>
      <h3 class="mb-3">Access denied</h3>
      <p class="text-muted">You do not have permission to access this page.</p>
      <a class="btn btn-outline-primary mt-2" routerLink="/">Back to home</a>
    </div>
  `,
})
export class NotAuthorised {}
