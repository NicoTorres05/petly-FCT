import { Component, OnInit } from '@angular/core';
import { CarritoService } from '../../services/carrito.service';
import { Carrito } from '../../models/carrito.model';

import { Usuario } from '../../models/usuario.model'
import { CommonModule } from '@angular/common';
import { TokenService } from '../../services/token.service';
import { Router, RouterModule } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {AuthService} from '../../services/auth.service';
import {Producto} from '../../models/producto.model';



@Component({
  selector: 'app-carrito-page',
  imports: [CommonModule, RouterModule],
  templateUrl: './carrito-page.html',
  standalone: true,
  styleUrls: ['./carrito-page.css']
})
export class CarritoPage implements OnInit {
  carrito!: Carrito;
  usuario: any = null;

  constructor(
    private carritoService: CarritoService,
    private tokenService: TokenService,
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.usuario = this.tokenService.getUserData();
    console.log(this.usuario)
    this.cargarCarrito();

  }


  cargarCarrito() {
    this.carritoService.getCarritoActivo().subscribe(data => {
      this.carrito = data;
      console.log(this.carrito)
    });
  }

  agregarProducto(productId: number) {
    console.log("Producto ID que se envía:", productId);

    this.carritoService.addToCarrito(productId, 1).subscribe({
      next: (res) => {
        this.cargarCarrito();
      },
      error: (err) => console.error('Error al agregar al carrito:', err)
    });
  }


  eliminarProducto(productId: number) {
    console.log(productId)
    this.carritoService.removeFromCarrito(productId).subscribe(() => {
      this.cargarCarrito();
    });
  }

  eliminarUno(productId: number) {
    console.log("Eliminar 1:", productId);

    this.carritoService.removeOne(productId, 1).subscribe({
      next: () => this.cargarCarrito(),
      error: (err) => console.error('Error al eliminar una unidad:', err)
    });
  }


  comprar() {
    this.carritoService.checkout().subscribe({
      next: (resp) => {
        alert(resp.mensaje);
        this.cargarCarrito();
      },
      error: (err) => {
        alert(err.error.mensaje || 'No se pudo completar la compra');
      }
    });
  }

}
