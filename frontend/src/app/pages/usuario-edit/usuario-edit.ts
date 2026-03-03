import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { TokenService } from '../../services/token.service';
import { UsuarioService } from '../../services/usuario.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'edit-profile-data',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './edit-profile-data.html',
  styleUrls: ['./edit-profile-data.css']
})
export class EditProfileData implements OnInit {

  perfilForm!: FormGroup;
  selectedFile: File | null = null;
  usuario: any = null;
  previewUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private tokenService: TokenService,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.perfilForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      tipo: ['', Validators.required],
      contrasena: [''],
      direccion: [''],
      telefono: [''],
      saldo: [''],
      pfp: ['']
    });
    let userId!: number;
    const routeId = this.route.snapshot.paramMap.get('id');

    if (routeId) {
      userId = +routeId;
    } else {
      const userData = this.tokenService.getUserData();
      if (!userData?.id) {
        console.warn("No hay usuario logueado");
        return;
      }
      userId = userData.id;
    }

    this.usuarioService.getUserById(userId).subscribe({
      next: (user) => {
        this.usuario = user;
        console.log("Usuario cargado:", user);
        this.perfilForm.patchValue({
          nombre: user.nombre,
          email: user.email,
          direccion: user.direccion,
          telefono: user.telefono,
          tipo: user.tipo,
          saldo: user.saldo,
          pfp: user.pfp
        });
        if (user.pfp) {
          this.previewUrl = 'http://localhost:8080' + user.pfp;
        }
      },
      error: (err) => {
        console.error("Error cargando usuario:", err);
      }
    });
  }

  submit(): void {
    console.log(this.perfilForm.value);

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

    const userId = this.usuario.id;

    if (!this.perfilForm.value.contrasena) {
      delete this.perfilForm.value.contrasena;
    }
    this.subirFoto()
    this.usuarioService.updateUser(userId, this.perfilForm.value).subscribe({
      next: () => {
        Swal.fire({
          title: 'Usuario actualizado',
          text: 'Los datos han sido modificados.',
          icon: 'success',
          confirmButtonText: 'Ok'
        });
        this.router.navigate(['/usuarios/list']);
      },
      error: (err) => {
        console.error(err);
        Swal.fire({
          title: 'Error',
          text: 'No se pudo actualizar al usuario.',
          icon: 'error',
          confirmButtonText: 'Aceptar'
        });
      }
    });
  }


  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;
    this.selectedFile = file;
    const reader = new FileReader();
    reader.onload = () => {
      this.previewUrl = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  subirFoto() {
    if (!this.selectedFile) return;

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.usuarioService.subirFoto(this.usuario.id, formData).subscribe({
      next: (response: any) => {
        this.usuario.pfp = response.pfp;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  eliminarFoto() {
    this.usuario.pfp = null;
    this.previewUrl = null;

    this.usuarioService.eliminarFoto(this.usuario.id).subscribe({
      next: () => {
        console.log("Foto eliminada correctamente");
      },
      error: err => {
        console.error("Error al eliminar la foto", err);
      }
    });
  }
}
