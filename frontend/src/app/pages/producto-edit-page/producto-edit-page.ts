import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { Location } from '@angular/common';
import { Categoria } from '../../models/categoria.model';
import { CategoriaService } from '../../services/categoria.service'
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-producto-edit-page',
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule
  ],
  templateUrl: './producto-edit-page.html',
  standalone: true,
  styleUrl: './producto-edit-page.css'
})
export class ProductoEditPage implements OnInit {
  productForm: FormGroup;
  categorias: Categoria[] = [];
  producto!: Producto;
  categoriaSelected: number | null = null;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
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

  ngOnInit(): void {
    this.cargarCategorias();
    this.cargarProducto();
  }

  cargarCategorias(): void {
    this.categoriaService.getAll().subscribe({
      next: (data) => (this.categorias = data),
      error: (err) => console.error('Error cargando categorías', err)
    });
  }

  cargarProducto(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.productoService.find(Number(id)).subscribe({
        next: (data) => {
          this.producto = data;
          console.log('Producto cargado:', data);
          this.productForm.patchValue(data);
        },
        error: (err) => console.error('Error cargando producto', err)
      });
    }
  }

  onSubmit(): void {

    console.log('hola')
    if (this.productForm.invalid) return;

    const productoDTO = this.productForm.value;
    this.error = null;

    if (this.producto && this.producto.id) {
      this.productoService.update(this.producto.id, productoDTO).subscribe({
        next: (productoActualizado) => {
          Swal.fire({
            title: '¡Éxito!',
            text: `Producto "${productoActualizado.nombre}" actualizado correctamente`,
            icon: 'success',
            confirmButtonText: 'Aceptar'
          }).then(() => {
            this.router.navigate(['/productos']);
          });
        },
        error: (err) => {
          console.error(err);
          this.error = 'Error al actualizar el producto. Por favor, inténtalo de nuevo.';
        }
      });
    } else {

      this.productoService.create(productoDTO).subscribe({
        next: (productoCreado) => {
          Swal.fire({
            title: '¡Éxito!',
            text: `Producto "${productoCreado.nombre}" creado correctamente`,
            icon: 'success',
            confirmButtonText: 'Aceptar'
          }).then(() => {
            this.router.navigate(['/productos']);
          });
        },
        error: (err) => {
          console.error(err);
          this.error = 'Error al crear el producto. Por favor, inténtalo de nuevo.';
        }
      });
    }
  }


  eliminarProducto(): void {
    if (!this.producto) return;

    Swal.fire({
      title: '¿Eliminar producto?',
      text: 'Esta acción no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.productoService.delete(this.producto.id).subscribe({
          next: () => {
            Swal.fire('Eliminado', 'El producto ha sido eliminado.', 'success');
            this.location.back();
          },
          error: (err) => {
            console.error(err);
            Swal.fire('Error', 'No se pudo eliminar el producto.', 'error');
          }
        });
      }
    });
  }
}
