package petly.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import petly.dto.ProductoDTO;
import petly.model.Producto;
import petly.model.Usuario;
import petly.repository.ProductoRepository;
import petly.service.ProductoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoRepository productoRepository;

    public ProductoController(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Producto> all(@RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            log.info("Accediendo a productos de la categoría con ID: " + categoriaId);
            return this.productoService.findByCategoriaId(categoriaId);
        }
        log.info("Accediendo a todos los productos");
        return this.productoService.all();
    }

    @GetMapping("/{id}")
    public Producto one(@PathVariable("id") Long id) {
        return this.productoService.one(id);
    }

    @PostMapping()
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.create(dto));
    }

    @PutMapping("/{id}")
    public Producto replace(@PathVariable("id") Long id, @RequestBody Producto producto) {
        return this.productoService.replace(id, producto);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable("id") Long id) {
        this.productoService.delete(id);
    }

    @GetMapping("/buscar")
    public List<Producto> buscarProductos(@RequestParam String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @PostMapping("/{id}/foto")
    public ResponseEntity<Map<String, String>> uploadFoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String url = productoService.guardarFoto(id, file);
            Map<String, String> response = new HashMap<>();
            response.put("pict", url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}/foto")
    public ResponseEntity<?> eliminarFoto(@PathVariable Long id) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);

        if (optionalProducto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Producto no encontrado");
        }

        Producto producto = optionalProducto.get();

        if (producto.getFoto() == null || producto.getFoto().equals("") || producto.getFoto().equals("null")) {
            return ResponseEntity.ok(Map.of("message", "El producto no tiene foto para eliminar"));
        }

        String rutaFoto = "uploads/prods/" + producto.getFoto();

        File archivo = new File(rutaFoto);
        if (archivo.exists()) {
            archivo.delete();
        }

        producto.setFoto(null);
        productoRepository.save(producto);

        return ResponseEntity.ok(Map.of("message", "Foto eliminada correctamente"));
    }
}