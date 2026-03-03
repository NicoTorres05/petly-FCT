package petly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nombre;
    private String email;
    private String direccion;
    private String telefono;
    private String rol;
    private Double saldo;
    private String contrasena;
    private String pfp;
}
