import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';


@Component({
  standalone: true,
  selector: 'app-productos-page',
  templateUrl: './productos-page.html',
  styleUrls: ['./productos-page.css'],
  imports: [CommonModule, RouterModule, FormsModule]
})
export class ProductosPageComponent implements OnInit {
  productos: Producto[] = [];

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.cargarProductos();
  }

  cargarProductos(): void {
    this.productoService.getAll().subscribe({
      next: (data) => {
        this.productos = data;
        console.log('Productos cargados:', data);
      },
      error: (err) => {
        console.error('Error cargando productos', err)
      }
    });
  }
}

