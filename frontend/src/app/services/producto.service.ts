import { Injectable } from '@angular/core';
import { HttpClient, HttpParams  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto.model';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private url = 'http://localhost:8080/productos';

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:8080/categoria';

  getAll(categoriaId?: number): Observable<Producto[]> {
    let params = new HttpParams();
    if (categoriaId) {
      params = params.set('categoriaId', categoriaId.toString());
      return this.http.get<Producto[]>(this.url, {params});
    } else {
      return this.http.get<Producto[]>(this.url);
    }
  }

  create(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.url, producto);
  }

  find(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.url}/${id}`);
  }

  buscarProductosPorNombre(nombre: string) {
    return this.http.get<Producto[]>(`${this.url}/buscar`, { params: { nombre } });
  }


  delete(id: number): Observable<Producto> {
    return this.http.delete<Producto>(`${this.url}/${id}`);
  }

  update(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.url}/${id}`, producto);
  }
}
