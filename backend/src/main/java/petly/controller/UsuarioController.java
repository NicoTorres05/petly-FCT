package petly.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import petly.dto.UsuarioLogDTO;
import petly.dto.UsuarioRegDTO;
import petly.model.Usuario;
import petly.service.TokenBlacklistService;
import petly.service.UsuarioService;
import petly.seguridad.jwt.SecurityConfig.*;
import petly.service.TokenService;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final TokenBlacklistService tokenBlacklistService;
    private final TokenService tokenService;

    public UsuarioController(UsuarioService usuarioService, TokenBlacklistService tokenBlacklistService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.tokenService = tokenService;
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

    @PutMapping("/{id:[0-9]+}")
    public Usuario replaceUsers(@PathVariable("id") Long id, @RequestBody Usuario user) {
        return this.usuarioService.replace(id, user);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id:[0-9]+}")
    public void deleteUsers(@PathVariable("id") Long id) {
        this.usuarioService.delete(id);
    }



    @GetMapping("/me")
    public ResponseEntity<Usuario> getCurrentUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal(); // ahora es Jwt
        Long userId = jwt.getClaim("id"); // extraemos el id desde los claims

        Usuario usuarioCompleto = usuarioService.findById(userId);
        return ResponseEntity.ok(usuarioCompleto);
    }


    @PutMapping("/me")
    public ResponseEntity<Usuario> updateMe(@RequestBody Usuario dto, Authentication auth) {
        Usuario user = usuarioService.updateUser(auth.getName(), dto);
        return ResponseEntity.ok(user);
    }



}