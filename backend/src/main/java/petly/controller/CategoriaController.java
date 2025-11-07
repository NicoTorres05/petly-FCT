package petly.controller;

import org.springframework.http.ResponseEntity;
import petly.model.Producto;
import petly.repository.CategoriaRepository;
import petly.dto.CategoriaDTO;

import petly.model.Categoria;
import petly.service.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import petly.service.ProductoService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;
    private CategoriaRepository categoriaRepository;
    private ProductoService productoService;

    public CategoriaController(ProductoService productoService, CategoriaService categoriaService, CategoriaRepository categoriaRepository) {
        this.categoriaService = categoriaService;
        this.categoriaRepository = categoriaRepository;
        this.productoService = productoService;
    }

    @GetMapping()
    public List<CategoriaDTO> getCategorias() {
        return categoriaRepository.findAllWithProductCount();
    }

    @GetMapping("/productos")
    public List<Producto> getProductsByCategory(@RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            return productoService.findByCategoriaId(categoriaId);
        }
        return productoService.all();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> one(@PathVariable Long id) {
        Optional<CategoriaDTO> categoriaOpt = categoriaRepository.findOneWithProductCount(id);
        return categoriaOpt
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Categoria newCategory(@RequestBody Categoria categoria) {
        return this.categoriaService.save(categoria);
    }

    @PutMapping("/{id}")
    public Categoria replace(@PathVariable Long id, @RequestBody Categoria categoria) {
        return this.categoriaService.replace(id, categoria);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id) {
        this.categoriaService.delete(id);
    }
}