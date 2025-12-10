import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Usuario } from '../../models/usuario.model'
import {CarritoService} from '../../services/carrito.service';
import {AuthService} from '../../services/auth.service';
import {UsuarioService} from '../../services/usuario.service';
import {TokenService} from '../../services/token.service';
import { BuscarService } from '../../services/buscar.service';
import { FormsModule } from '@angular/forms';




@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './header.html',
  standalone: true,
  styleUrl: './header.css'
})
export class Header implements OnInit {

  usuario: Usuario | null = null;
  searchTerm: string = '';

  constructor(
    private authService: AuthService,
    private usuarioService: UsuarioService,
    private buscarService: BuscarService,
    private tokenService: TokenService,
  ) {}

  ngOnInit() {
    const token = localStorage.getItem('authToken');
    if (token) {
      this.authService.me().subscribe({
        next: (user) => {
          this.usuario = user;
          console.log('Usuario logeado:', user);
        },
        error: () => {
          console.warn('Token inválido, limpiando sesión');
          localStorage.removeItem('authToken');
          this.usuario = null;
        }
      });
    } else {
      this.usuario = null;
    }
  }


  buscarProductos() {
    const term = this.searchTerm.trim();
    this.buscarService.setSearchTerm(term);
  }
}
