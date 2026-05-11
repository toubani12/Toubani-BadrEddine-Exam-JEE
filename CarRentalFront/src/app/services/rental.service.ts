import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_BASE_URL, Rental, RentalRequest } from '../model/model';

@Injectable({ providedIn: 'root' })
export class RentalService {
  private http = inject(HttpClient);
  private base = `${API_BASE_URL}/rentals`;

  list(filters: { vehicleId?: string; customerEmail?: string; activeOnly?: boolean } = {}): Observable<Rental[]> {
    let params = new HttpParams();
    if (filters.vehicleId) params = params.set('vehicleId', filters.vehicleId);
    if (filters.customerEmail) params = params.set('customerEmail', filters.customerEmail);
    if (filters.activeOnly) params = params.set('activeOnly', 'true');
    return this.http.get<Rental[]>(this.base, { params });
  }

  create(req: RentalRequest): Observable<Rental> {
    return this.http.post<Rental>(this.base, req);
  }

  start(id: string): Observable<Rental> {
    return this.http.post<Rental>(`${this.base}/${id}/start`, null);
  }

  complete(id: string, actualReturnDate?: string): Observable<Rental> {
    const params = actualReturnDate
      ? new HttpParams().set('actualReturnDate', actualReturnDate)
      : undefined;
    return this.http.post<Rental>(`${this.base}/${id}/complete`, null, { params });
  }

  cancel(id: string): Observable<Rental> {
    return this.http.post<Rental>(`${this.base}/${id}/cancel`, null);
  }
}
