package petly.service;

import org.springframework.stereotype.Service;
import petly.model.Producto;
import petly.repository.ProductoDAO;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoDAO productoDAO;

    public ProductoService(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    public void crearProducto(Producto producto) {
        // Regla de negocio mínima: nombre y precio obligatorio
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().signum() <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        productoDAO.create(producto);
    }

    public void actualizarProducto(Producto producto) {
        productoDAO.update(producto);
    }

    public void eliminarProducto(int id) {
        Producto p = productoDAO.find(id);
        if (p != null) {
            productoDAO.delete(p);
        }
    }

    public Producto obtenerProducto(int id) {
        return productoDAO.find(id);
    }

    public List<Producto> obtenerTodos() {
        return productoDAO.findAll();
    }
}
