import { Producto } from './producto.model';
import { Usuario } from './usuario.model';

export interface Comentario {
  id: number;
  titulo: string;
  mensaje: string;
  clasificacion: number;
  fechaPublicacion?: string;
  idProducto: number;
  idUsuario: number;


  producto?: Producto;
  usuario?: Usuario;
}
