package petly.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import petly.exceptions.UserNotFoundException;
import petly.model.Usuario;
import petly.dto.UsuarioRegDTO;
import petly.repository.UsuarioRepository;
import petly.seguridad.jwt.SecurityConfig;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> all() {
        return this.usuarioRepository.findAll();
    }

    public Usuario save(Usuario user) {
        return this.usuarioRepository.save(user);
    }

    public Usuario one(Long id) {
        return this.usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Usuario replace(Long id, Usuario user) {
        return this.usuarioRepository.findById(id).map(u -> (id.equals(user.getId()) ?
                        this.usuarioRepository.save(user) : null))
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void delete(Long id) {
        this.usuarioRepository.findById(id).map(u -> {
                    this.usuarioRepository.delete(u);
                    return u;
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Usuario register(UsuarioRegDTO dto) {
        Usuario user = new Usuario();
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setEmail(dto.getEmail());
        // Ciframos la contraseña antes de guardar:
        user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        // Convertimos el String a tu enum Roles (asegúrate de que coincide):
        user.setTipo(Usuario.tipo.valueOf(dto.getTipo()));

        return usuarioRepository.save(user);
    }


}
