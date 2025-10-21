import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';

@Component({
  selector: 'app-producto-detail-page',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './producto-detail-page.html',
  standalone: true,
  styleUrls: ['./producto-detail-page.css']
})
export class ProductoDetailPage implements OnInit {

  producto!: Producto;  // objeto individual

  constructor(
    private route: ActivatedRoute,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.loadProducto();
  }

  get descuentoValido(): boolean {
    // @ts-ignore
    return this.producto ? this.producto.descuento > 0 : false;
  }


  loadProducto(): void {
    const id = this.route.snapshot.paramMap.get('id'); // obtiene el id de la URL
    if (id) {
      this.productoService.find(Number(id)).subscribe({
        next: (data) => {
          this.producto = data;
          console.log('Producto cargado:', data);
        },
        error: (err) => {
          console.error('Error cargando producto', err);
        }
      });
    }
  }
}
