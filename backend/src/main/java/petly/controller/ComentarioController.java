package petly.controller;

import org.springframework.web.bind.annotation.*;
import petly.model.Comentario;
import petly.service.ComentarioService;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@CrossOrigin(origins = "http://localhost:4200")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public List<Comentario> all() {
        return comentarioService.all();
    }

    @GetMapping("/{id}")
    public Comentario one(@PathVariable Long id) {
        return comentarioService.one(id);
    }

    @PostMapping
    public Comentario save(@RequestBody Comentario comentario) {
        return comentarioService.save(comentario);
    }

    @PutMapping("/{id}")
    public Comentario replace(
            @PathVariable Long id,
            @RequestBody Comentario comentario
    ) {
        return comentarioService.replace(id, comentario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        comentarioService.delete(id);
    }
}