package petly.model;

import jakarta.persistence.*;
import lombok.*;

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


    /**
     * Relación ManyToOne con la entidad User.
     * Un usuario puede tener varios carritos (historial de compras, por ejemplo).
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Relación OneToMany con la entidad CartItem.
     * Un carrito puede tener varios items.
     * CascadeType.ALL asegura que al guardar el carrito se guarden los items automáticamente.
     */
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoProds> carProductos;

    @Column(name = "precio_total")
    private Double precioTotal;


    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private estado status; // ACTIVE, COMPLETED, CANCELLED

    public enum estado {
        ACTIVO,       // carrito en uso
        COMPLETEDO,    // carrito ya comprado
        CANCELLEDO     // carrito eliminado/cancelado
    }

}