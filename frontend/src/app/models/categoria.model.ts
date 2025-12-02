import { Producto } from './producto.model';

export interface Categoria {
  id: number;
  nombre: string;
  descripcion?: string;
  productos?: Producto[];
}
