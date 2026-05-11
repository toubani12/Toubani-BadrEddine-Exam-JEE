import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { API_BASE_URL, AuthResponse, LoginRequest, RegisterRequest, Role } from '../model/model';

const STORAGE_KEY = 'car-rental-auth';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  private http = inject(HttpClient);

  private readonly _state = signal<AuthResponse | null>(this.restore());
  readonly state = this._state.asReadonly();
  readonly isLoggedIn = computed(() => this._state() !== null);
  readonly roles = computed<Role[]>(() => this._state()?.roles ?? []);
  readonly fullName = computed(() => {
    const s = this._state();
    return s ? `${s.firstName ?? ''} ${s.lastName ?? ''}`.trim() : '';
  });

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${API_BASE_URL}/auth/login`, req)
      .pipe(tap((res) => this.persist(res)));
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${API_BASE_URL}/auth/register`, req)
      .pipe(tap((res) => this.persist(res)));
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this._state.set(null);
  }

  token(): string | null {
    return this._state()?.token ?? null;
  }

  hasRole(role: Role): boolean {
    return this.roles().includes(role);
  }

  hasAnyRole(...roles: Role[]): boolean {
    return roles.some((r) => this.hasRole(r));
  }

  private persist(res: AuthResponse): void {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(res));
    this._state.set(res);
  }

  private restore(): AuthResponse | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as AuthResponse;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  }
}
