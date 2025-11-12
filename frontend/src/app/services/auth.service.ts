import { inject, Injectable } from '@angular/core';
import { environment } from '../../environment/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, tap } from 'rxjs';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  tokenService = inject(TokenService);
  private url = environment.apiUrl;
  private tokenKey = 'authToken';
  private baseUrl = 'http://localhost:8080/usuarios';


  private loggedIn = new BehaviorSubject<boolean>(this.tokenService.loggedIn());
  authStatus = this.loggedIn.asObservable();

  constructor(private http: HttpClient) { }

  login(form: Object): Observable<any> {
    // envía los datos del form login al backend
    return this.http.post(`${this.url}/login`, form).pipe(
      tap((response: any) => {
        if (response.token) {
          // guarda token en almacenamiento local
          this.tokenService.set(response.token);
          // cambia estado a "logueado"
          this.changeAuthStatus(true);
        }
      })
    )
  }


  me(): Observable<any> {
    const token = this.tokenService.get();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get(`${this.baseUrl}/me`, { headers });
  }



  logout(): void {
    // cambia estado a "no logueado"
    this.changeAuthStatus(false);

    this.tokenService.remove();

    // obtiene token actual
    const token = this.tokenService.get();
    if (token) {
      const httpOptions = {
        headers: new HttpHeaders({
          'Authorization': `Bearer ${token}`
        })
      };

      // envía POST al backend para cerrar sesión
      this.http.post(`${this.url}/logout`, {}, httpOptions).subscribe({
        next: () => {
          console.log('Sesión cerrada en el backend');
        },
        error: (err) => {
          console.error('Error al cerrar sesión en el backend:', err);
        }
      });
    }
  }

  isAdmin(): boolean {
    const token = localStorage.getItem('authToken');
    if (!token) return false;

    // decodifica el token
    const payload = JSON.parse(atob(token.split('.')[1]));
    // true si el rol es "admin"
    return payload.role === 'admin';
  }

  register(form: Object): Observable<any> {
    // datos registro al backend
    return this.http.post(`${this.url}/register`, form).pipe(
      tap((response: any) => {
        if (response.token) {
          this.tokenService.set(response.token);
          this.changeAuthStatus(true);
        }
      })
    );
  }

  isAuthenticated(): boolean {
    // obtiene el token
    const token = localStorage.getItem(this.tokenKey);
    // true si existe, false si no
    return !!token;
  }

  changeAuthStatus(value: boolean) {
    this.loggedIn.next(value);
  }

  updateUser(data: any): Observable<any> {
    const token = this.tokenService.get();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.put(`${this.baseUrl}/me`, data, { headers });
  }




  getUserData(): Observable<any> {
    // obtiene el token
    const token = localStorage.getItem('authToken');
    if (!token) {
      console.warn('No se encontró un token en el almacenamiento local.');
      return of(null);
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const userId = payload.id;
      const headers = new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
      // pide los datos del usuario usando ID
      return this.http.get(`${this.url}/${userId}`, { headers });
    } catch (error) {
      console.error('Error al decodificar el token:', error);
      return of(null);
    }
  }

}
