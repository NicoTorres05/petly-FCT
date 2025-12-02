import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth.service'

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})
export class Profile implements OnInit {
  usuario: any = null;
  url: string = 'http://localhost:8080/';

  constructor(
    private tokenService: TokenService,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.usuario = this.tokenService.getUserData();
    console.log('Datos del usuario:', this.usuario);
  }

  logout(): void {
    this.tokenService.remove();
    this.router.navigate(['/usuarios/login']);

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
}
