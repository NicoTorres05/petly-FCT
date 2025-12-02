package petly.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import java.math.BigDecimal;
import java.util.Map;

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
    public ResponseEntity<String> addProductoAlCarrito(Authentication auth, @RequestBody CarritoAddDTO request) {

        if (auth == null) return ResponseEntity.status(401).body("Usuario no autenticado");

        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.addProducto(usuario.getId(), request.getProductoId(), request.getCantidad());
        return ResponseEntity.ok("Producto agregado al carrito");
    }


    @DeleteMapping("/delete")
    public ResponseEntity<String> eliminarCarrito(Authentication auth, @PathVariable Long id) {
        if (auth == null) return ResponseEntity.status(401).body("Usuario no autenticado");
        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.delete(usuario.getId());
        return ResponseEntity.ok("Carrito eliminado");
    }

    @PostMapping("/delete-one")
    public ResponseEntity<String> eliminarUno(Authentication auth, @RequestBody CarritoAddDTO request, HttpServletRequest req) {
        System.out.println("Auth: " + auth);
        System.out.println("Header Authorization: " + req.getHeader("Authorization"));
        if (auth == null) return ResponseEntity.status(401).body("Usuario no autenticado");

        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.restarProducto(usuario.getId(), request.getProductoId(), request.getCantidad());

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> eliminarItem(Authentication auth, @PathVariable Long id) {
        if (auth == null) return ResponseEntity.status(401).body("Usuario no autenticado");
        String email = auth.getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        carritoService.deleteItem(usuario.getId(), id);
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

        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(usuario.getId(), Carrito.Estado.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));


        BigDecimal total = carritoService.calcTotal(carrito);
        BigDecimal saldo = usuario.getSaldo();

        if (saldo.compareTo(total) < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensaje", "Saldo insuficiente").toString());
        }

        usuario.setSaldo(saldo.subtract(total));
        usuarioRepository.save(usuario);

        carritoService.completarCarrito(usuario.getId());

        return ResponseEntity.ok(Map.of(
                "mensaje", "Carrito completado correctamente",
                "saldoActual", usuario.getSaldo()
        ).toString());

    }



}