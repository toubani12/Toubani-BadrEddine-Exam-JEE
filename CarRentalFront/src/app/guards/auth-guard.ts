import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthenticationService } from '../services/authentication.service';
import { Role } from '../model/model';

export const authGuard: CanActivateFn = (route) => {
  const auth = inject(AuthenticationService);
  const router = inject(Router);

  if (!auth.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const required = route.data?.['roles'] as Role[] | undefined;
  if (required && required.length > 0 && !auth.hasAnyRole(...required)) {
    router.navigate(['/not-authorised']);
    return false;
  }
  return true;
};
