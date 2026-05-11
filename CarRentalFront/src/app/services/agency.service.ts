import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_BASE_URL, Agency, Vehicle } from '../model/model';

@Injectable({ providedIn: 'root' })
export class AgencyService {
  private http = inject(HttpClient);
  private base = `${API_BASE_URL}/agencies`;

  list(): Observable<Agency[]> {
    return this.http.get<Agency[]>(this.base);
  }

  get(id: string): Observable<Agency> {
    return this.http.get<Agency>(`${this.base}/${id}`);
  }

  create(payload: Agency): Observable<Agency> {
    return this.http.post<Agency>(this.base, payload);
  }

  update(id: string, payload: Agency): Observable<Agency> {
    return this.http.put<Agency>(`${this.base}/${id}`, payload);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  vehiclesOf(id: string): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.base}/${id}/vehicles`);
  }
}
