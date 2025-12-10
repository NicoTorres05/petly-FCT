import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { CarritoService } from '../../services/carrito.service';
import { BuscarService } from '../../services/buscar.service';
import { AuthService } from '../../services/auth.service';
import { UsuarioService } from '../../services/usuario.service';
import {Usuario} from '../../models/usuario.model';


@Component({
  standalone: true,
  selector: 'app-productos-page',
  templateUrl: './productos-page.html',
  styleUrls: ['./productos-page.css'],
  imports: [CommonModule, RouterModule, FormsModule]
})
export class ProductosPageComponent implements OnInit {
  productos: Producto[] = [];
  categoriaId?: number;
  usuario: Usuario | null = null;


  constructor(private authService: AuthService, private usuarioService: UsuarioService, private router: Router, private route: ActivatedRoute, private productoService: ProductoService, private buscarService: BuscarService, private carritoService: CarritoService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const categoriaId = params['categoriaId'] ? Number(params['categoriaId']) : undefined;
      this.categoriaId = categoriaId;
      this.cargarProductos(categoriaId);
    });

    this.buscarService.getSearchTerm().subscribe(term => {
      if (term && term.trim() !== '') {
        this.productoService.buscarProductosPorNombre(term).subscribe(res => {
          this.productos = res;
        });
      } else {
        this.cargarProductos(this.categoriaId);
      }
    });

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



  cargarProductos(categoriaId?: number): void {
    this.productoService.getAll(categoriaId).subscribe({
      next: (data) => {
        this.productos = data;
        console.log('Productos cargados:', data);
      },
      error: (err) => {
        console.error('Error cargando productos', err);
      }
    });
  }

  agregarAlCarrito(producto: Producto) {

    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/usuarios/login']);
      return;
    }

    this.authService.me().subscribe({
      next: () => {
        this.carritoService.addToCarrito(producto.id, 1)
          .subscribe({
            next: (res) => alert(res),
            error: (err) => console.error(err)
          });
      },
      error: () => {
        localStorage.removeItem('token');
        this.router.navigate(['/usuarios/login']);
      }
    });
  }


}

