import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token.service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../services/auth.service'
import { UsuarioService } from '../../services/usuario.service'

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
    private usuarioService: UsuarioService,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const userData = this.tokenService.getUserData();
    if (!userData || !userData.id) {
      console.warn("No hay usuario logueado");
      return;
    }
    const userId = userData.id;
    this.usuarioService.getUserById(userId).subscribe({
      next: (user) => {
        this.usuario = user;
        console.log("Usuario cargado desde BD:", user);
      },
      error: (err) => {
        console.error("Error cargando usuario:", err);
      }
    });
    this.usuarioService.currentUser$.subscribe(user => {
      if (user) {
        this.usuario = user;
      }
    });    console.log('Datos del usuario:', this.usuario);
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
          window.location.reload();

        },
        error: (err) => {
          console.error('Error al cerrar sesión en el backend:', err);
        }
      });
    }
}
}
