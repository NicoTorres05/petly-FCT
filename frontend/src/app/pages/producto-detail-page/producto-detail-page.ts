import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { RouterModule } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Categoria } from '../../models/categoria.model';
import { CategoriaService } from '../../services/categoria.service'

@Component({
  selector: 'app-producto-detail-page',
  imports: [
    CommonModule,
    RouterModule,
    TranslateModule
  ],
  templateUrl: './producto-detail-page.html',
  standalone: true,
  styleUrls: ['./producto-detail-page.css']
})
export class ProductoDetailPage implements OnInit {

  producto!: Producto;

  constructor(
    private route: ActivatedRoute,
    private productoService: ProductoService
  ) {}

  ngOnInit(): void {
    this.loadProducto();
  }

 // TODO sistema de descuento


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
