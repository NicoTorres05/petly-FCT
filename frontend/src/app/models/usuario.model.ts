export interface UsuarioModel {
  id: number;
  nombre: string;
  email: string;
  contrasena: string;
  direccion?: string;
  telefono?: string;
  pfp?: string; // foto de perfil
  tipo?: string;
  saldo: number;
  preferencias?: string[]; // lista de tipos de productos/animales que le interesan
  // comentarios?: Comentario[]; // si los comentarios son otra entidad
  // historialCompras?: Compra[]; // opcional, si quieres relacionar compras
  fechaRegistro?: string; // o Date
}
