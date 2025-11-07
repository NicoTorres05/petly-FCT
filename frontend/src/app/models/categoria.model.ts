import { Producto } from './producto.model';

export interface Categoria {
  id: number;
  nombre: string;
  descripcion?: string; // opcional porque puede ser null o vacío
  productos?: Producto[]; // relación con productos (opcional para evitar bucles)
}
