package petly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import petly.model.Producto;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String foto;
    private BigDecimal precio;
    private Integer stock;
    private int descuento;
    private Boolean activo;

    // incluir info de la categoría
    private Long categoriaId;
}