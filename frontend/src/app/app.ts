import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar';
import { Header } from './components/header/header'
import { routes } from './app.routes';
import { Component, OnInit } from '@angular/core';
import { TokenService } from './services/token.service';
import { AuthService } from './services/auth.service';


@Component({
  standalone: true,
  selector: 'app-root',
  styleUrls: ['./app.css'],
  template: `
    <app-header></app-header>
    <app-navbar></app-navbar>

    <router-outlet></router-outlet>

  `,
  imports: [CommonModule, RouterModule, NavbarComponent, Header]
})


export class App implements OnInit{

  constructor(
    private tokenService: TokenService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    if (this.tokenService.isValid()) {
      console.log('Token válido, el usuario sigue logueado');
      this.authService.changeAuthStatus(true);
    } else {
      console.log('Token inválido o expirado');
      this.authService.changeAuthStatus(false);
    }
  }
}





