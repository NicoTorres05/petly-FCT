import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { CarritoService } from '../../services/carrito.service';


@Component({
  standalone: true,
  selector: 'app-productos-page',
  templateUrl: './productos-page.html',
  styleUrls: ['./productos-page.css'],
  imports: [CommonModule, RouterModule, FormsModule]
})
export class ProductosPageComponent implements OnInit {
  productos: Producto[] = [];
  userId = 7;

  constructor(private route: ActivatedRoute, private productoService: ProductoService, private carritoService: CarritoService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const categoriaId = params['categoriaId'];
      console.log('categoriaId recibido:', categoriaId);
      this.cargarProductos(categoriaId ? Number(categoriaId) : undefined);
    });
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
    console.log("Producto ID que se envía:", producto.id);
    this.carritoService.addToCarrito(producto.id, 1)
      .subscribe({
        next: (res) => alert(res),
        error: (err) => console.error('Error al agregar al carrito:', err)
      });
  }

}

