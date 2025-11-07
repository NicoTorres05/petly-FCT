package petly.controller;

import jakarta.validation.Valid;
import petly.dto.UsuarioRegDTO;
import petly.model.Usuario;
import petly.service.UsuarioService;
import petly.seguridad.jwt.SecurityConfig.*;

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

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> all() {
        log.info("Accediendo a todos los usuarios");
        return this.usuarioService.all();
    }

    @GetMapping("/{id}")
    public Usuario one(@PathVariable("id") Long id) {
        return this.usuarioService.one(id);
    }

    @PostMapping
    public Usuario newUsers(@RequestBody Usuario user) {
        return this.usuarioService.save(user);
    }

    @PutMapping("/{id}")
    public Usuario replaceUsers(@PathVariable("id") Long id, @RequestBody Usuario user) {
        return this.usuarioService.replace(id, user);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteUsers(@PathVariable("id") Long id) {
        this.usuarioService.delete(id);
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody UsuarioRegDTO registerFullUserDTO) throws IOException {
        Usuario registered = usuarioService.register(registerFullUserDTO);
        return ResponseEntity.ok(registered);
    }


    //TODO /login

    //TODO /logout

}