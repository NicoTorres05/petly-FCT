package petly.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
    @NotBlank
    private String nombre;
    private String descripcion;
    private String foto;
    @Positive
    private BigDecimal precio;
    @Min(0)
    private Integer stock;
    private int descuento;
    private Boolean activo;

    private Long categoriaId;
}