package petly.mapper;


import petly.dto.UsuarioDTO;
import petly.model.Usuario;

import java.math.BigDecimal;

public class UsuarioMapper {

    public static Usuario fromDTO(UsuarioDTO dto) {
        System.out.println(dto.getRol());
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setDireccion(dto.getDireccion());
        usuario.setTelefono(Integer.valueOf(dto.getTelefono()));
        usuario.setSaldo(BigDecimal.valueOf(dto.getSaldo()));
        usuario.setContrasena(dto.getContrasena());

        if (dto.getRol() != null) {
            try {
                usuario.setRol(Usuario.rol.valueOf(dto.getRol().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Tipo de usuario inválido: " + dto.getRol());
            }
        }

        return usuario;
    }

}

