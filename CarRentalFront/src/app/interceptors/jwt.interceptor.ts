import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

import { AuthenticationService } from '../services/authentication.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthenticationService);
  const router = inject(Router);

  const token = auth.token();
  const cloned = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(cloned).pipe(
    catchError((err) => {
      if (err?.status === 401 && auth.isLoggedIn()) {
        auth.logout();
        router.navigate(['/login']);
      } else if (err?.status === 403) {
        router.navigate(['/not-authorised']);
      }
      return throwError(() => err);
    })
  );
};
