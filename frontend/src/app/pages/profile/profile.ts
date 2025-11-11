import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})
export class Profile implements OnInit {
  usuario: any = null;

  constructor(private tokenService: TokenService) {}

  ngOnInit(): void {
    this.usuario = this.tokenService.getUserData();
    console.log('Datos del usuario:', this.usuario);
  }
}
