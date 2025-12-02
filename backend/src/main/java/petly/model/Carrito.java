package petly.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProds> carProductos = new ArrayList<>();;

    @Column(name = "precio_total")
    private BigDecimal precioTotal;


    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public enum Estado {
        ACTIVO,
        COMPLETADO,
        CANCELADO
    }


    public CarritoProds findItem(Long productoId) {
        return carProductos.stream()
                .filter(cp -> cp.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);
    }

}