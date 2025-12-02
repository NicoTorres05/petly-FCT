import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Usuario } from '../models/usuario.model'


@Injectable({ providedIn: 'root' })
export class UsuarioService {

  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}

  getUserById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`http://localhost:8080/usuarios/${id}`);
  }

/**
  loadCurrentUser(): Observable<Usuario> {
    const token = localStorage.getItem('token');
    console.log('Token enviado:', token);

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<Usuario>('http://localhost:8080/usuarios/me', { headers });
  }

  getCurrentUser(): Usuario | null {
    return this.currentUserSubject.value;
  }

  setCurrentUser(user: Usuario) {
    this.currentUserSubject.next(user);
  }
    **/
}
