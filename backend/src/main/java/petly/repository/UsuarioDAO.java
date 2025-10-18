package petly.repository;

import petly.model.Usuario;

import java.util.List;

public interface UsuarioDAO {
    public void create(Usuario usuario);
    public void update(Usuario usuario);
    public void delete(Usuario usuario);
    public Usuario find(int id);
    public List<Usuario> findAll();
}
