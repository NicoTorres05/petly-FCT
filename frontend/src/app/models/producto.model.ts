import { Categoria } from './categoria.model';

export interface Producto {
  id: number;
  nombre: string;
  descripcion?: string;
  foto?: string;
  categoria?: Categoria;  // relación con categoría
  // tipoAnimal?: Mascota; // lo puedes activar si creas ese modelo luego
  precio: number;
  ventas?: number;
  stock?: number;
  descuento?: number;
  fechaCreacion?: string; // normalmente se envía como texto ISO desde el backend
  activo?: boolean;
}
