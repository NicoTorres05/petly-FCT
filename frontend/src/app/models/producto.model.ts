import { Categoria } from './categoria.model';

export interface Producto {
  id: number;
  nombre: string;
  descripcion?: string;
  foto?: string;
  categoria?: Categoria;
  precio: number;
  ventas?: number;
  stock?: number;
  descuento?: number;
  fechaCreacion?: string;
  activo?: boolean;
}
