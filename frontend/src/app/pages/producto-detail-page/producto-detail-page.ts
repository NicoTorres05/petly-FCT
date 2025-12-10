import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { ProductoService } from '../../services/producto.service';
import { ComentarioService } from '../../services/comentario.service';
import { TokenService } from '../../services/token.service';
import { Producto } from '../../models/producto.model';
import { Comentario } from '../../models/comentario.model';

@Component({
  selector: 'producto-detail-page',
  templateUrl: './producto-detail-page.html',
  standalone: true,
  imports: [CommonModule, DatePipe, ReactiveFormsModule, RouterModule],
  styleUrls: ['./producto-detail-page.css']
})
export class ProductoDetailPage implements OnInit {

  producto!: Producto;
  comentarios: Comentario[] = [];
  commentForm!: FormGroup;
  usuario: any = null;

  constructor(
    private route: ActivatedRoute,
    private productoService: ProductoService,
    private comentarioService: ComentarioService,
    private tokenService: TokenService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.commentForm = this.fb.group({
      titulo: ['', Validators.required],
      mensaje: ['', Validators.required],
      clasificacion: [5, [Validators.required, Validators.min(1), Validators.max(5)]]
    });

    this.loadUsuario();
    this.loadProducto();
  }

  loadUsuario() {
    const userData = this.tokenService.getUserData();
    if (!userData?.id) {
      console.warn("No hay usuario logueado");
      return;
    }
    this.usuario = userData;
  }

  loadProducto(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) return;

    this.productoService.find(Number(id)).subscribe({
      next: (data) => {
        this.producto = data;
        console.log('Producto cargado:', data);
        this.loadComentarios();
      },
      error: (err) => {
        console.error('Error cargando producto', err);
      }
    });
  }

  loadComentarios(): void {
    if (!this.producto?.id) return;

    this.comentarioService.getComentarios().subscribe({
      next: (data) => {
        this.comentarios = data.filter(c => c.idProducto === this.producto.id);
      },
      error: (err) => {
        console.error('Error cargando comentarios', err);
      }
    });
  }

  submit(): void {
    if (!this.usuario) {
      Swal.fire('Error', 'Debes estar logueado para comentar', 'error');
      return;
    }

    if (this.commentForm.invalid) {
      this.commentForm.markAllAsTouched();
      Swal.fire('Error', 'Revisa los campos del comentario', 'error');
      return;
    }
    const commentDTO = this.commentForm.value;

    const newComment = {
      ...commentDTO,
      fechaPublicacion: new Date().toISOString(),
      idProducto: this.producto.id,
      idUsuario: this.usuario.id
    };


    this.comentarioService.createComentario(newComment).subscribe({
      next: (saved) => {
        Swal.fire('Éxito', 'Comentario publicado', 'success');
        this.comentarios.push(saved);
        this.commentForm.reset({ clasificacion: 5 });
      },
      error: (err) => {
        console.error(err);
        Swal.fire('Error', 'No se pudo publicar el comentario', 'error');
      }
    });
  }
}
