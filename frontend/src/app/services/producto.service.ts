import { Injectable } from '@angular/core';
import { HttpClient, HttpParams  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto.model';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private apiUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:8080/categoria';

  getAll(categoriaId?: number): Observable<Producto[]> {
    let params = new HttpParams();
    if (categoriaId) {
      params = params.set('categoriaId', categoriaId.toString());
      return this.http.get<Producto[]>(this.apiUrl, {params});
    } else {
      return this.http.get<Producto[]>(this.apiUrl);
    }
  }

  create(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  find(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  delete(id: number): Observable<Producto> {
    return this.http.delete<Producto>(`${this.apiUrl}/${id}`);
  }

  update(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }






}
