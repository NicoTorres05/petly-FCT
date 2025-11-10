import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../services/token.service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, TranslateModule],
  templateUrl: './login.html',
  standalone: true,
  styleUrl: './login.css'
})
export class Login {
  loginForm: FormGroup;
  hasLoaded: boolean = true;

  error = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router,
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  submit() {

    if (this.loginForm.valid) {
      Swal.fire({
        title: 'Bienvenido',
        text: 'Has iniciado sesión correctamente.',
        icon: 'success',
        confirmButtonText: 'Continuar'
      });
      this.router.navigateByUrl('/');
    } else if (this.loginForm.invalid) {
      Swal.fire({
        title: 'Error',
        text: 'Revisa los campos marcados.',
        icon: 'error',
        confirmButtonText: 'Entendido'
      });

      this.loginForm.markAllAsTouched();

      return;
    }

    this.hasLoaded = false;
    this.authService.login(this.loginForm.value).subscribe({
      next: (rtn) => {

        const validToken = this.tokenService.handle(rtn.token);

        if (validToken) {
          this.authService.changeAuthStatus(true);
          console.log("Logged in successfully");

        }
      },
      error: (error) => {
        console.error(error);
        this.hasLoaded = true;
        this.handleError(error);
      },
    });
  }

  handleError(error: any) {
    this.error = error.error.error;
  }
}
