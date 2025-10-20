import { HomePageComponent } from './pages/home-page/home-page';
import { ProductosPageComponent } from './pages/productos-page/productos-page';

export const routes = [
  { path: '', component: HomePageComponent },
  { path: 'productos', component: ProductosPageComponent },
  { path: '**', redirectTo: '' }
];
