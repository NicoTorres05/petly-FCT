import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto.model';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private apiUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  find(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

}
