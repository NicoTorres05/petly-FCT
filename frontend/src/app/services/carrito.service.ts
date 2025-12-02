import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Carrito } from '../models/carrito.model';
import { TokenService } from './token.service';

@Injectable({ providedIn: 'root' })
export class CarritoService {
  private apiUrl = 'http://localhost:8080/api/carrito';
  private tokenService = inject(TokenService);

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.tokenService.get();
    if (!token) throw new Error('Usuario no autenticado');
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getCarritoActivo(): Observable<Carrito> {
    return this.http.get<Carrito>(this.apiUrl, { headers: this.getAuthHeaders() });
  }

  addToCarrito(productoId: number, cantidad: number) {
    const body = { productoId, cantidad };
    return this.http.post(`${this.apiUrl}/add`, body, { headers: this.getAuthHeaders(), responseType: 'text' });
  }

  removeFromCarrito(productId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${productId}`, { headers: this.getAuthHeaders(), responseType: 'text' });
  }

  removeOne(productId: number, cantidad: number) {
    return this.http.post(`${this.apiUrl}/delete-one`,  { productoId: productId, cantidad: cantidad}, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }




  checkout(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/checkout`, {}, { headers, responseType: 'text'  });
  }

}
