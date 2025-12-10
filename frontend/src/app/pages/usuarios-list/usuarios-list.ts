import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsuarioService } from '../../services/usuario.service'
import {AuthService} from '../../services/auth.service';
import {ProductoService} from '../../services/producto.service';
import {BuscarService} from '../../services/buscar.service';
import { Router, RouterModule } from '@angular/router';
import { RouterLink } from '@angular/router';
import {CarritoService} from '../../services/carrito.service';
import {Producto} from '../../models/producto.model';
import { Usuario } from '../../models/usuario.model'
import Swal from 'sweetalert2';
import { Location } from '@angular/common';



@Component({
  selector: 'app-usuarios-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './usuarios-list.html',
  standalone: true,
  styleUrl: './usuarios-list.css'
})
export class UsuariosList implements OnInit {
  usuarios: Usuario[] = [];
  usuario: any = null;

  constructor(
  //  private authService: AuthService,
    private usuarioService: UsuarioService,
  private location: Location,
  private router: Router,
  //  private route: ActivatedRoute,
  //  private productoService: ProductoService,
  //  private buscarService: BuscarService,
  //  private carritoService: CarritoService
    ) {}

  ngOnInit(): void {
    this.cargarProductos()
  }

  cargarProductos(categoriaId?: number): void {
    this.usuarioService.getAll().subscribe({
      next: (data) => {
        this.usuarios = data;
        console.log('Productos cargados:', data);
      },
      error: (err) => {
        console.error('Error cargando productos', err);
      }
    });
  }

  eliminarUsuario(id: number): void {
    this.usuarioService.getUserById(id).subscribe({
      next: (usuario) => {
        if (!usuario) return;

        Swal.fire({
          title: '¿Eliminar usuario?',
          text: 'Esta acción no se puede deshacer',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Sí, eliminar',
          cancelButtonText: 'Cancelar'
        }).then((result) => {
          if (result.isConfirmed) {
            this.usuarioService.deleteUser(usuario.id).subscribe({
              next: () => {
                Swal.fire('Eliminado', 'El usuario ha sido eliminado.', 'success');
              },
              error: (err) => {
                console.error(err);
                Swal.fire('Error', 'No se pudo eliminar el usuario.', 'error');
              }
            });
          }
        });
      },
      error: (err) => {
        console.error(err);
        Swal.fire('Error', 'No se pudo obtener el usuario.', 'error');
      }
    });
  }


}
