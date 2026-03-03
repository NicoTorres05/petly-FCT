package petly.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;
import petly.dto.UsuarioDTO;
import petly.dto.UsuarioLogDTO;
import petly.dto.UsuarioRegDTO;
import petly.model.Usuario;
import petly.service.TokenBlackListService;
import petly.service.UsuarioService;
import petly.seguridad.jwt.SecurityConfig.*;
import petly.service.TokenService;
import petly.repository.UsuarioRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final TokenBlackListService tokenBlacklistService;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService, TokenBlackListService tokenBlacklistService, TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> all() {
        log.info("Accediendo a todos los usuarios");
        return this.usuarioService.all();
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody UsuarioRegDTO registerFullUserDTO) throws IOException {
        Usuario registered = usuarioService.register(registerFullUserDTO);
        return ResponseEntity.ok(registered);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginUserDTO) {
        LoginResponse loginResponse = usuarioService.authenticate(loginUserDTO);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Usuario> one(@PathVariable("id") Long id) {
        Usuario usuario = usuarioService.one(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }


    @PostMapping
    public Usuario newUsers(@RequestBody Usuario user) {
        return this.usuarioService.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario updated = usuarioService.replace(id, usuarioDTO);
        return ResponseEntity.ok(updated);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id:[0-9]+}")
    public void deleteUsers(@PathVariable("id") Long id) {
        this.usuarioService.delete(id);
    }



    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userId = jwt.getClaim("id");

        Usuario usuarioCompleto = usuarioService.findById(userId);
        return ResponseEntity.ok(usuarioCompleto);
    }


    @PutMapping("/me")
    public ResponseEntity<Usuario> updateMe(@RequestBody Usuario dto, Authentication auth) {
        Usuario user = usuarioService.updateUser(auth.getName(), dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/foto")
    public ResponseEntity<Map<String, String>> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String url = usuarioService.guardarFoto(id, file);
            Map<String, String> response = new HashMap<>();
            response.put("pfp", url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}/foto")
    public ResponseEntity<?> eliminarFoto(@PathVariable Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        if (usuario.getPfp() == null || usuario.getPfp().equals("") || usuario.getPfp().equals("null")) {
            return ResponseEntity.ok(Map.of("message", "El usuario no tiene foto para eliminar"));
        }

        String rutaFoto = "uploads/users/" + usuario.getPfp();

        File archivo = new File(rutaFoto);
        if (archivo.exists()) {
            archivo.delete();
        }

        usuario.setPfp(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("message", "Foto eliminada correctamente"));
    }


}