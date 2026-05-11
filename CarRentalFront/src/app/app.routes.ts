import { Routes } from '@angular/router';

import { Login } from './login/login';
import { NewCustmer } from './new-custmer/new-custmer';
import { NotAuthorised } from './not-authorised/not-authorised';
import { AdminTemplate } from './admin-template/admin-template';
import { Accounts } from './accounts/accounts';
import { Customers } from './customers/customers';
import { Rentals } from './rentals/rentals';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'new-custmer', component: NewCustmer },
  { path: 'not-authorised', component: NotAuthorised },
  {
    path: '',
    component: AdminTemplate,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'customers', pathMatch: 'full' },
      { path: 'accounts', component: Accounts },
      { path: 'customers', component: Customers },
      {
        path: 'rentals',
        component: Rentals,
        canActivate: [authGuard],
        data: { roles: ['ROLE_EMPLOYE', 'ROLE_ADMIN'] },
      },
    ],
  },
  { path: '**', redirectTo: '' },
];
