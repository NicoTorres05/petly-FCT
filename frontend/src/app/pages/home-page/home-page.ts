import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-home-page',
  templateUrl: './home-page.html',
  styleUrls: ['./home-page.css'],
  imports: [CommonModule, RouterModule]
})
export class HomePageComponent implements OnInit {

  productosDestacados: Producto[] = [];

  constructor(private productoService: ProductoService) {}

  ngOnInit(): void {
    this.cargarProductosDestacados();
  }

  cargarProductosDestacados(): void {

    this.productoService.getAll().subscribe({
      next: data => {
        this.productosDestacados = data.slice(0, 4);
      },
      error: err => console.error('Error cargando productos', err)
    });
  }
}
