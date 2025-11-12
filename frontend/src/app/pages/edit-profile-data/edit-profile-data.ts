import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'edit-profile-data',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './edit-profile-data.html',
  styleUrls: ['./edit-profile-data.css']
})
export class EditProfileData implements OnInit {
  perfilForm!: FormGroup;
  usuario: any = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.usuario = this.tokenService.getUserData();

    // Inicializamos el formulario con los valores actuales
    this.perfilForm = this.fb.group({
      nombre: [this.usuario?.name || '', Validators.required],
      email: [this.usuario?.sub || this.usuario?.email || '', [Validators.required, Validators.email]],
      direccion: [this.usuario?.direction || ''],
      telefono: [this.usuario?.phone || ''],
    });
  }

  submit(): void {
    if (this.perfilForm.invalid) {
      this.perfilForm.markAllAsTouched();
      Swal.fire({
        title: 'Error',
        text: 'Revisa los campos marcados.',
        icon: 'error',
        confirmButtonText: 'Entendido'
      });
      return;
    }

    this.authService.updateUser(this.perfilForm.value).subscribe({
      next: (updatedUser) => {
        Swal.fire({
          title: 'Perfil actualizado',
          text: 'Tus datos han sido guardados correctamente.',
          icon: 'success',
          confirmButtonText: 'Continuar'
        });

        // Solo actualiza el token si el backend te devuelve uno
        if (updatedUser.token) {
          this.tokenService.set(updatedUser.token);
        }

        this.router.navigate(['/perfil']);
      },
      error: (err) => {
        console.error(err);
        Swal.fire({
          title: 'Error',
          text: 'Ocurrió un error al actualizar tu perfil.',
          icon: 'error',
          confirmButtonText: 'Entendido'
        });
      }
    });
  }

}
