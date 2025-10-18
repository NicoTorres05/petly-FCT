package petly.service;


import org.springframework.stereotype.Service;
import petly.model.Usuario;
import petly.repository.UsuarioDAO;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void newUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        usuarioDAO.create(usuario);
    }

    public List<Usuario> listAll() {
        return usuarioDAO.findAll();
    }

    public Usuario one(int id) {
        return usuarioDAO.find(id);
    }

    public void updateUsuario(Usuario usuario) {
        usuarioDAO.update(usuario);
    }

    public void deleteUsuario(int id) {
        Usuario u = usuarioDAO.find(id);
        if (u != null) {
            usuarioDAO.delete(u);
        }
    }
}
