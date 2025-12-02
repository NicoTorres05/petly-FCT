import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Usuario } from '../../models/usuario.model'
import {CarritoService} from '../../services/carrito.service';
import {AuthService} from '../../services/auth.service';
import {UsuarioService} from '../../services/usuario.service';
import {TokenService} from '../../services/token.service';



@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterModule],
  templateUrl: './header.html',
  standalone: true,
  styleUrl: './header.css'
})
export class Header implements OnInit {

  usuario: Usuario | null = null;

  constructor(
    private usuarioService: UsuarioService,
    private tokenService: TokenService,
  ) {}

  ngOnInit() {

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
    });

  }
}
