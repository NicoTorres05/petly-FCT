import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { RouterModule } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Carousel } from 'bootstrap';



@Component({
  standalone: true,
  selector: 'app-home-page',
  templateUrl: './home-page.html',
  styleUrls: ['./home-page.css'],
  imports: [CommonModule, RouterModule, TranslateModule]
})
export class HomePageComponent implements OnInit {

  constructor(private productoService: ProductoService) {


  }


  productosDestacados: Producto[] = [];



  ngOnInit(): void {
    this.cargarProductosDestacados();
  }

  cargarProductosDestacados(): void {
    this.productoService.getAll().subscribe({
      next: data => {
        this.productosDestacados = data.slice(0, 4);
        console.log(this.productosDestacados)
      },
      error: err => console.error('Error cargando productos', err)
    });
  }

  ngAfterViewInit(): void {
    const carouselElement = document.querySelector('#carouselExampleIndicators');
    if (carouselElement) {
      new Carousel(carouselElement, {
        interval: 3000,
        ride: 'carousel'
      });
    }
  }
}
