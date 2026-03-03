package petly.service;


import org.springframework.stereotype.Service;
import petly.exceptions.ComentarioNotFoundException;
import petly.model.Comentario;
import petly.repository.ComentarioRepository;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    public List<Comentario> all() {
        return this.comentarioRepository.findAll();
    }

    public Comentario save(Comentario comentario) {
        return this.comentarioRepository.save(comentario);
    }

    public Comentario one(Long id) {
        return this.comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException(id));
    }

    public Comentario replace(Long id, Comentario comentario) {
        return this.comentarioRepository.findById(id)
                .map(c -> (id.equals(comentario.getId()) ?
                        this.comentarioRepository.save(comentario) : null))
                .orElseThrow(() -> new ComentarioNotFoundException(id));
    }

    public void delete(Long id) {
        this.comentarioRepository.findById(id)
                .map(c -> {
                    this.comentarioRepository.delete(c);
                    return c;
                })
                .orElseThrow(() -> new ComentarioNotFoundException(id));
    }
}