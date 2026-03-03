package petly.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import petly.dto.UsuarioDTO;
import petly.dto.UsuarioLogDTO;
import petly.exceptions.UserNotFoundException;
import petly.mapper.UsuarioMapper;
import petly.model.Usuario;
import petly.dto.UsuarioRegDTO;
import petly.repository.UsuarioRepository;
import petly.seguridad.jwt.SecurityConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final Path uploadDir = Paths.get("uploads");

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public Usuario updateUser(String username, Usuario dto) {
        Usuario user = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setNombre(dto.getNombre());
        user.setDireccion(dto.getDireccion());
        user.setTelefono(dto.getTelefono());

        return usuarioRepository.save(user);
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

    public Usuario replace(Long id, UsuarioDTO dto) {
        Usuario user = UsuarioMapper.fromDTO(dto);

        return usuarioRepository.findById(id)
                .map(u -> {
                    u.setNombre(user.getNombre());
                    u.setEmail(user.getEmail());
                    u.setDireccion(user.getDireccion());
                    u.setTelefono(user.getTelefono());
                    u.setRol(user.getRol());
                    System.out.println(user.getRol());
                    u.setSaldo(user.getSaldo());

                    if (user.getContrasena() != null && !user.getContrasena().isEmpty()) {
                        u.setContrasena(passwordEncoder.encode(user.getContrasena()));
                    }

                    return usuarioRepository.save(u);
                })
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
        user.setDireccion(String.valueOf(dto.getDireccion()));
        user.setTelefono(dto.getTelefono());
        user.setEmail(dto.getEmail());
        user.setEmail(dto.getEmail());
        user.setRol(Usuario.rol.NORMAL);
        user.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        user.setFechaRegistro(LocalDate.now());
        return usuarioRepository.save(user);
    }

    public SecurityConfig.LoginResponse authenticate(SecurityConfig.LoginDTO input) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password()));
        Usuario user = (Usuario) authentication.getPrincipal();
        String token = tokenService.generateToken(authentication, null);
        return new SecurityConfig.LoginResponse(token, user.getNombre(), user.getEmail());
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public String guardarFoto(Long id, MultipartFile file) throws IOException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Path uploadDir = Paths.get("uploads/users/");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalName = file.getOriginalFilename();
        String safeName = originalName.replaceAll(" ", "_");

        String filename = "usuario_" + id + "_" + safeName;

        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        usuario.setPfp("/uploads/users/" + filename);
        usuarioRepository.save(usuario);

        return usuario.getPfp();
    }
}