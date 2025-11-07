import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar';
import { Header } from './components/header/header'
import { routes } from './app.routes';


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
export class App {}
