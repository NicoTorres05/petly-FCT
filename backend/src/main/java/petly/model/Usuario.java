package petly.model;


/* USUARIO

NOMBRE
EMAIL
CONTRASENA
DIRECCION
TIPO
SALDO
IMAGENPERFIL
FECHAREGISTRO
*/

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int id;

    @NotNull
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String contrasena;

    private String direccion;

    @Enumerated(EnumType.STRING)
    private tipo tipo;

    public enum tipo {
        NORMAL,
        PREMIUM,
        ADMIN
    }


    private BigDecimal saldo = BigDecimal.ZERO;

    private String pfp;


    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;


    // @ManyToOne
    // @JoinColumn(name = "mascota_id")
    // private Mascota mascota;

    //@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Comentario> comentarios;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Carrito> carritos;


}
