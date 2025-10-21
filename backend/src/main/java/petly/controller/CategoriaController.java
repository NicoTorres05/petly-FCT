package petly.controller;

import petly.model.Categoria;
import petly.service.CategoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> all() {
        log.info("Accediendo a todas las categorías");
        return this.categoriaService.all();
    }

    @GetMapping("/{id}")
    public Categoria one(@PathVariable Long id) {
        return this.categoriaService.one(id);
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