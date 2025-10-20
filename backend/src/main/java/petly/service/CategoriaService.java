package petly.service;

import org.springframework.stereotype.Service;
import petly.model.Categoria;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public void crearCategoria(Categoria categoria) {
        // Ejemplo de regla: nombre obligatorio
        if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        categoriaDAO.create(categoria);
    }

    public void actualizarCategoria(Categoria categoria) {
        categoriaDAO.update(categoria);
    }

    public void eliminarCategoria(int id) {
        Categoria cat = categoriaDAO.find(id);
        if (cat != null) {
            categoriaDAO.delete(cat);
        }
    }

    public Categoria obtenerCategoria(int id) {
        return categoriaDAO.find(id);
    }

    public List<Categoria> obtenerTodas() {
        return categoriaDAO.findAll();
    }
}
