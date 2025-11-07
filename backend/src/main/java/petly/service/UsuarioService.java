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
import petly.service.TokenService;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
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

    public SecurityConfig.LoginResponse authenticate(SecurityConfig.LoginDTO input) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password()));
        Usuario user = (Usuario) authentication.getPrincipal();
        String token = tokenService.generateToken(authentication, null); // Genera un nuevo token, el segundo parámetro es el token actual (null en este caso)
        return new SecurityConfig.LoginResponse(token, user.getNombre(), user.getEmail());
    }


}
