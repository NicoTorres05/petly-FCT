package petly.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import java.util.Set;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String nombre;

    @Size(max = 500)
    private String descripcion;

    private String imagen;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Producto> productos;


}