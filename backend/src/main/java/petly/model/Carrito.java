package petly.model;

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
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProds> carProductos = new ArrayList<>();;

    @Column(name = "precio_total")
    private BigDecimal precioTotal;


    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado; // ACTIVE, COMPLETED, CANCELLED

    public enum Estado {
        ACTIVO,       // carrito en uso
        COMPLETADO,    // carrito ya comprado
        CANCELADO     // carrito eliminado/cancelado
    }

}