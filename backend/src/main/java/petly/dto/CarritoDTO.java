package petly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import petly.model.CarritoProds;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarritoDTO {
    private Long id;
    private BigDecimal precioTotal;
    private List<CarritoProdsDTO> productos;
    private Long usuarioId; // solo el id
}
