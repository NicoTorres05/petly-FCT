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
    return this.http.post(`${this.baseUrl}/login`, form).pipe(
      tap((response: any) => {
        if (response.token) {
          this.tokenService.set(response.token);
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
    this.changeAuthStatus(false);

    this.tokenService.remove();

    const token = this.tokenService.get();
    if (token) {
      const httpOptions = {
        headers: new HttpHeaders({
          'Authorization': `Bearer ${token}`
        })
      };

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

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.role === 'admin';
  }

  register(form: Object): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, form).pipe(
      tap((response: any) => {
        if (response.token) {
          this.tokenService.set(response.token);
          this.changeAuthStatus(true);
        }
      })
    );
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem(this.tokenKey);
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
      return this.http.get(`${this.url}/${userId}`, { headers });
    } catch (error) {
      console.error('Error al decodificar el token:', error);
      return of(null);
    }
  }

}
