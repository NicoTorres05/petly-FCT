import { HomePageComponent } from './pages/home-page/home-page';
import { ProductosPageComponent } from './pages/productos-page/productos-page';
import { ProductoDetailPage } from './pages/producto-detail-page/producto-detail-page'
import { ProductoEditPage } from './pages/producto-edit-page/producto-edit-page'
import { ProductoCreatePage } from './pages/producto-create-page/producto-create-page'
import { Register } from './pages/register/register'
import { Login } from './pages/login/login'
import { Profile } from './pages/profile/profile'
import { AuthGuard } from './guard/auth.guard';
import { EditProfileData } from './pages/edit-profile-data/edit-profile-data'

export const routes = [
  { path: '', component: HomePageComponent },
  { path: 'productos', component: ProductosPageComponent },
  { path: 'productos/crear', component: ProductoCreatePage },
  { path: 'productos/editar/:id', component: ProductoEditPage },
  { path: 'productos/:id', component: ProductoDetailPage },
  { path: 'categorias/productos', component: ProductosPageComponent },
  { path: 'usuarios/registro', component: Register},
  { path: 'usuarios/login', component: Login},
  { path: 'usuarios/profile', component: Profile, canActivate: [AuthGuard]},
  { path: 'usuarios/profile/edit', component: EditProfileData, canActivate: [AuthGuard]},



  { path: '**', redirectTo: '' }
];
