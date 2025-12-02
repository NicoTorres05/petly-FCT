package petly.controller;

import petly.model.Producto;
import petly.model.Usuario;
import petly.service.ProductoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
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

    @PostMapping
    public Producto newProducto(@RequestBody Producto producto) {
        return this.productoService.save(producto);
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
}