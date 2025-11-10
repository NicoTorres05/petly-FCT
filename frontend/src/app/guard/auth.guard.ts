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
  ) { }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const tokenValue = this.tokenService.get();
    if (!tokenValue) {
      this.router.navigate(['login']);
      return of(false);
    }

    const requiredRole = next.data['role'];
    const token = { token: tokenValue };

    return this.authService.me(token).pipe(
      map((user) => {
        if (requiredRole && user.role !== requiredRole) {
          this.router.navigate(['login']);
          return false;
        }
        return true;
      }),
      catchError((error) => {
        console.error(error);
        this.router.navigate(['login']);
        return of(false);
      })
    );
  }
}
