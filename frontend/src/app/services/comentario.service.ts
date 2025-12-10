import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comentario } from '../models/comentario.model';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {

  private baseUrl = 'http://localhost:8080/comentarios';

  constructor(private http: HttpClient) { }

  getComentarios(): Observable<Comentario[]> {
    return this.http.get<Comentario[]>(this.baseUrl);
  }

  getComentario(id: number): Observable<Comentario> {
    return this.http.get<Comentario>(`${this.baseUrl}/${id}`);
  }

  createComentario(comentario: Comentario): Observable<Comentario> {
    return this.http.post<Comentario>(this.baseUrl, comentario);
  }

  updateComentario(id: number, comentario: Comentario): Observable<Comentario> {
    return this.http.put<Comentario>(`${this.baseUrl}/${id}`, comentario);
  }

  deleteComentario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
