import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { Navbar } from '../navbar/navbar';

@Component({
  selector: 'app-admin-template',
  imports: [RouterOutlet, Navbar],
  template: `
    <app-navbar />
    <main class="py-3">
      <router-outlet />
    </main>
  `,
})
export class AdminTemplate {}
