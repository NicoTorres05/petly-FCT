import { Usuario } from './usuario.model'
import { CarritoProds } from './carritoProds.model'

export interface Carrito {
  id: number;
  usuario: Usuario;
  productos: CarritoProds[];
  precioTotal: number;
  estado: 'ACTIVO' | 'COMPLETADO' | 'CANCELADO';
}
