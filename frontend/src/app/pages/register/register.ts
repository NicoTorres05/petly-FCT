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
    NgIf,
    ReactiveFormsModule
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
    this.userForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contrasena: ['', Validators.required],
      direccion: [''],
      telefono: [''],
      pfp: ['']
    });
  }

  submit(): void {
    if (this.userForm.valid) {

      this.authService.register(this.userForm.value).subscribe({
        next: () => {

          this.authService.login({
            email: this.userForm.value.email,
            password: this.userForm.value.contrasena
          }).subscribe({
            next: (rtn) => {

              const validToken = this.authService.tokenService.handle(rtn.token);

              if (validToken) {

                this.authService.changeAuthStatus(true);


                Swal.fire({
                  title: 'Bienvenido',
                  text: 'Has iniciado sesión automáticamente.',
                  icon: 'success',
                  confirmButtonText: 'Continuar'
                });


                this.router.navigate(['/']);
              }
            },
            error: (err) => {
              console.error('Error iniciando sesión automáticamente', err);


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

      this.userForm.markAllAsTouched();
    }
  }

}
