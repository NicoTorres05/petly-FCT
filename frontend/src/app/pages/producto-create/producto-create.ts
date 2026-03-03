import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';


import { Producto } from '../../models/producto.model';
import { Categoria } from '../../models/categoria.model';
import { ProductoService } from '../../services/producto.service';
import { CategoriaService } from '../../services/categoria.service'

@Component({
  selector: 'app-producto-create-page',
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule
  ],
  templateUrl: './producto-create-page.html',
  standalone: true,
  styleUrl: './producto-create-page.css'
})
export class ProductoCreatePage implements OnInit {
  productForm: FormGroup;

  error: string | null = null;
  categorias: Categoria[] = [];

  //TODO script para que no puedas poner muchos decimales

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private productoService: ProductoService,
    private categoriaService: CategoriaService
  ) {
    this.productForm = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
      foto: [''],
      categoria: [null, Validators.required],
      precio: [0, [Validators.required, Validators.min(0)]],
      stock: [0, [Validators.required, Validators.min(0)]],
      descuento: [0, [Validators.min(0)]],
      ventas: [0],
      fechaCreacion: [new Date()],
      activo: [true]

    });
  }
  onSubmit() {
    const productoDTO = this.productForm.value;
    this.error = null;
    this.productoService.create(productoDTO).subscribe({
      next: (producto) => {
        const nombreProd = this.productForm.value.nombre;
        Swal.fire({
          title: '¡Éxito!',
          text: `Producto "${nombreProd}" creado correctamente`,
          icon: 'success',
          confirmButtonText: 'Aceptar'
        }).then(() => {
          this.productForm.reset({
            nombre: '',
            descripcion: '',
            foto: '',
            categoria: null,
            precio: 0,
            stock: 0,
            descuento: 0,
            ventas: 0,
            fechaCreacion: new Date()
          });
        });
      },
      error: (err) => {
        this.error = 'Error al crear el producto. Por favor, inténtalo de nuevo.';

        console.error(err);
      }
    });
  }
  ngOnInit(): void {
    this.cargarCategorias();
  }

  cargarCategorias(): void {
    this.categoriaService.getAll().subscribe({
      next: data => this.categorias = data,
      error: err => console.error('Error cargando productos', err)
    });
  }
}
