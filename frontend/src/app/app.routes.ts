import { HomePageComponent } from './pages/home-page/home-page';
import { ProductosPageComponent } from './pages/productos-page/productos-page';
import { ProductoDetailPage } from './pages/producto-detail-page/producto-detail-page'

export const routes = [
  { path: '', component: HomePageComponent },
  { path: 'productos', component: ProductosPageComponent },
  { path: 'productos/:id', component: ProductoDetailPage },

  { path: '**', redirectTo: '' }
];
