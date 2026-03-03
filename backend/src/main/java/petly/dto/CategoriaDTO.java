package petly.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import petly.model.Categoria;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    private Long id;
    @NotBlank
    private String nombre;
    private String descripcion;
    private Long numProductos;


    public CategoriaDTO(Categoria categoria) {
        this.nombre = categoria.getNombre();
        this.descripcion = categoria.getDescripcion();
        this.numProductos = (long) (categoria.getProductos() != null ? categoria.getProductos().size() : 0);
    }
}
