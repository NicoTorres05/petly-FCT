package petly.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegDTO {

        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotNull(message = "El teléfono es obligatorio")
        private Integer telefono;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "Debe ser un email válido")
        private String email;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
        private String contrasena;

        @NotBlank(message = "El rol es obligatorio")
        private String tipo;

        private String direccion;
}
