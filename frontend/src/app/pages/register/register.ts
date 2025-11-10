import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule  } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  templateUrl: './register.html',
  standalone: true,
  styleUrls: ['./register.css'],
  imports: [
    NgIf,                      // 👈 para usar *ngIf
    ReactiveFormsModule        // 👈 para usar [formGroup], formControlName, etc.
  ]
})
export class Register implements OnInit {
  userForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Inicializa el formulario según UsuarioModel
    this.userForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contrasena: ['', Validators.required],
      direccion: [''], // opcional
      telefono: [''],  // opcional
      pfp: ['']        // opcional, manejaremos archivo por separado si se desea
    });
  }

  submit(): void {
    if (this.userForm.valid) {
      // 1️⃣ Registramos al usuario
      this.authService.register(this.userForm.value).subscribe({
        next: () => {
          // 2️⃣ Si el registro es exitoso, hacemos login automático
          this.authService.login({
            email: this.userForm.value.email,
            password: this.userForm.value.contrasena
          }).subscribe({
            next: (rtn) => {
              // 3️⃣ Guardamos el token y validamos
              const validToken = this.authService.tokenService.handle(rtn.token);

              if (validToken) {
                // 4️⃣ Cambiamos el estado de autenticación global
                this.authService.changeAuthStatus(true);

                // 5️⃣ Mensaje de bienvenida
                Swal.fire({
                  title: 'Bienvenido',
                  text: 'Has iniciado sesión automáticamente.',
                  icon: 'success',
                  confirmButtonText: 'Continuar'
                });

                // 6️⃣ Redirigimos a la página principal
                this.router.navigate(['/']);
              }
            },
            error: (err) => {
              console.error('Error iniciando sesión automáticamente', err);

              // Si falla el login automático, redirige al login
              Swal.fire({
                title: 'Registro exitoso',
                text: 'Por favor inicia sesión manualmente.',
                icon: 'info',
                confirmButtonText: 'Aceptar'
              });
              this.router.navigate(['/login']);
            }
          });
        },
        error: (err) => {
          // Manejo de errores del registro
          if (err.status === 400) {
            Swal.fire({
              title: 'Error de validación',
              text: 'Por favor, revisa los datos ingresados.',
              icon: 'error',
              confirmButtonText: 'Entendido'
            });
          } else if (err.status === 409) {
            Swal.fire({
              title: 'Error',
              text: 'El email ya está registrado.',
              icon: 'error',
              confirmButtonText: 'Entendido'
            });
          } else {
            Swal.fire({
              title: 'Error',
              text: 'Ocurrió un error al registrar el usuario.',
              icon: 'error',
              confirmButtonText: 'Entendido'
            });
          }
          console.error(err);
        }
      });
    } else {
      // Marca todos los campos como tocados para mostrar errores de validación
      this.userForm.markAllAsTouched();
    }
  }

}
