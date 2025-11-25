package petly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoProdsDTO {
    private Long id;
    private Long productoId;
    private BigDecimal precio;
    private int cantidad;
    private String productoNombre;
}