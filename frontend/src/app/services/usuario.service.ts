import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Usuario } from '../models/usuario.model'
import { TokenService } from './token.service'
import {Producto} from '../models/producto.model';



@Injectable({ providedIn: 'root' })
export class UsuarioService {

  private currentUserSubject = new BehaviorSubject<Usuario | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  usuario: Usuario | null = null;
  private url = 'http://localhost:8080/usuarios';


  constructor(private http: HttpClient, private tokenService: TokenService) {}

  getAll(): Observable<Usuario[]> {
      return this.http.get<Usuario[]>(this.url);

  }

  getUserById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.url}/${id}`);
  }

  updateUser(id: number, data: any) {
    return this.http.put(`${this.url}/${id}`, data);
  }

  deleteUser(id: number): Observable<Usuario> {
    return this.http.delete<Usuario>(`${this.url}/${id}`)
  }

  subirFoto(userId: number, fileData: FormData): Observable<string> {
    return this.http.post<string>(`${this.url}/${userId}/foto`, fileData);
  }



  loggedIn() {
    const userData = this.tokenService.getUserData();
    if (!userData || !userData.id) {
      return;
    }
    const userId = userData.id;
    this.getUserById(userId).subscribe({
      next: (user) => {
        this.usuario = user;
        console.log("Usuario cargado desde BD:", user);
      },
      error: (err) => {
        console.error("Error cargando usuario:", err);
      }
    });
    this.currentUser$.subscribe(user => {
      if (user) {
        this.usuario = user;
      }
    });
  }

  eliminarFoto(id: number) {
    return this.http.delete(`http://localhost:8080/usuarios/${id}/foto`);
  }


}
