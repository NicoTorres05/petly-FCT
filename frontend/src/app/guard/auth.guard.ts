import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { TokenService } from '../services/token.service';
import { catchError, map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const tokenValue = this.tokenService.get();
    if (!tokenValue) {
      // No hay token → redirige al login
      this.router.navigate(['usuarios/login']);
      return of(false);
    }

    const requiredRole = next.data['role'];

    return this.authService.me().pipe(
      map((user) => {
        // Si la ruta tiene restricción de rol, verificamos
        if (requiredRole && user.role !== requiredRole) {
          this.router.navigate(['usuarios/login']);
          return false;
        }
        return true; // ✅ Usuario autenticado y permitido
      }),
      catchError((error) => {
        console.error('Error en AuthGuard:', error);

        // Solo redirige si el token está caducado o inválido
        if (error.status === 401 || error.status === 403) {
          this.tokenService.remove();
          this.router.navigate(['usuarios/login']);
        } else {
          // Si fue otro error (500, 404, etc.), deja al usuario pasar
          // o podrías mostrar un aviso sin cerrarle sesión
          console.warn('Error no crítico, manteniendo sesión.');
        }

        return of(false);
      })
    );
  }
}
