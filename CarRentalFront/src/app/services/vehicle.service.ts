import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_BASE_URL, Car, Motorcycle, Vehicle, VehicleStatus } from '../model/model';

@Injectable({ providedIn: 'root' })
export class VehicleService {
  private http = inject(HttpClient);
  private base = `${API_BASE_URL}/vehicles`;

  list(filters: { status?: VehicleStatus; agencyId?: string; brand?: string } = {}): Observable<Vehicle[]> {
    let params = new HttpParams();
    if (filters.status) params = params.set('status', filters.status);
    if (filters.agencyId) params = params.set('agencyId', filters.agencyId);
    if (filters.brand) params = params.set('brand', filters.brand);
    return this.http.get<Vehicle[]>(this.base, { params });
  }

  available(agencyId?: string): Observable<Vehicle[]> {
    const params = agencyId ? new HttpParams().set('agencyId', agencyId) : undefined;
    return this.http.get<Vehicle[]>(`${this.base}/available`, { params });
  }

  updateStatus(id: string, status: VehicleStatus): Observable<Vehicle> {
    return this.http.patch<Vehicle>(`${this.base}/${id}/status`, null, {
      params: new HttpParams().set('status', status),
    });
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  createCar(payload: Car, agencyId: string): Observable<Car> {
    return this.http.post<Car>(`${API_BASE_URL}/cars`, payload, {
      params: new HttpParams().set('agencyId', agencyId),
    });
  }

  createMotorcycle(payload: Motorcycle, agencyId: string): Observable<Motorcycle> {
    return this.http.post<Motorcycle>(`${API_BASE_URL}/motorcycles`, payload, {
      params: new HttpParams().set('agencyId', agencyId),
    });
  }
}
