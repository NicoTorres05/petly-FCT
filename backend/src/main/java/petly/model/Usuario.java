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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size()
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    private String contrasena;

    private String direccion;

    @Enumerated(EnumType.STRING)
    private UsuarioTipo tipo; // NORMAL, PREMIUM, ADMIN


    private BigDecimal saldo = BigDecimal.ZERO;

    private String pfp;


    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;


    // @ManyToOne
    // @JoinColumn(name = "mascota_id")
    // private Mascota mascota;

    //@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Comentario> comentarios;

    //@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Compra> historialCompras;


}
