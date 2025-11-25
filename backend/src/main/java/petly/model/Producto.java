package petly.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    private String foto;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties("productos")
    private Categoria categoria;



    @Positive
    private BigDecimal precio;

    private Integer stock;

    private int descuento;

    private int ventas;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private Boolean activo = true;
}
