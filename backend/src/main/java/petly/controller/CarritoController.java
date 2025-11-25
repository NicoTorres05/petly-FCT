package petly.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import petly.dto.CarritoAddDTO;
import petly.dto.CarritoDTO;
import petly.model.Carrito;
import petly.model.Usuario;
import petly.repository.CarritoRepository;
import petly.repository.UsuarioRepository;
import petly.service.CarritoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }


    @GetMapping
    public ResponseEntity<CarritoDTO> getCarrito(Authentication auth) {
        Carrito carrito = carritoService.getCarritoActivo(auth);
        CarritoDTO dto = carritoService.mapToDTO(carrito);
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/add")
    public ResponseEntity<String> addProductoAlCarrito(
            Authentication auth,
            @RequestBody CarritoAddDTO request) {

        if (auth == null) return ResponseEntity.status(401).body("Usuario no autenticado");

        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.addProducto(usuario.getId(), request.getProductoId(), request.getCantidad());
        return ResponseEntity.ok("Producto agregado al carrito");
    }


    @DeleteMapping
    public ResponseEntity<String> eliminarCarritoUsuario(@AuthenticationPrincipal Usuario usuario) {
        carritoService.delete(usuario.getId());
        return ResponseEntity.ok("Carrito eliminado");
    }



/**
    @GetMapping("/{id}")
    public Carrito one(@PathVariable("id") Long id) {
        return this.carritoService.one(id);
    }

**/

    @PutMapping("/{id}")
    public Carrito replace(@PathVariable("id") Long id, @RequestBody Carrito carrito) {
        return this.carritoService.replace(id, carrito);
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<?> addToCarrito(@PathVariable Long userId, @RequestBody CarritoAddDTO request) {
        System.out.println("Iniciando carrito");
        carritoService.addProducto(userId, request.getProductoId(), request.getCantidad());
        return ResponseEntity.ok("Producto agregado al carrito");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCarrito(@PathVariable("id") Long id) {
        this.carritoService.delete(id);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.completarCarrito(usuario.getId());

        return ResponseEntity.ok("Carrito completado correctamente");
    }



}