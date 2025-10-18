package petly.repository;

import petly.model.Categoria;

import java.util.List;

public interface CategoriaDAO {
    public void create(Categoria categoria);
    public void update(Categoria categoria);
    public void delete(Categoria categoria);
    public Categoria find(int id);
    public List<Categoria> findAll();
}
