package petly.repository;

import petly.model.Producto;

import java.util.List;

public interface ProductoDAO {
    public void create(Producto producto);
    public void update(Producto producto);
    public void delete(Producto producto);
    public Producto find(int id);
    public List<Producto> findAll();
}
