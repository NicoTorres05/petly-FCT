package petly.dto;


import petly.model.Categoria;

public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private int numProductos;


    public CategoriaDTO(Categoria categoria) {
        //this.id = categoria.getId();
        this.nombre = categoria.getNombre();
        this.descripcion = categoria.getDescripcion();
        this.imagen = categoria.getImagen();
        this.numProductos = categoria.getProductos() != null ? categoria.getProductos().size() : 0;
    }
}
