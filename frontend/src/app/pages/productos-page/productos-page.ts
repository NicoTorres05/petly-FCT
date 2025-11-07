import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
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

  constructor(private route: ActivatedRoute, private productoService: ProductoService) {}

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

}

