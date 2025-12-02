import { Producto } from './producto.model'

export interface CarritoProds {
  id: number;
  producto: Producto;
  cantidad: number;
  precio: number;
  productoId: number;
}
