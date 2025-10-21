package petly.service;

import org.springframework.stereotype.Service;
import petly.model.Categoria;

import petly.exceptions.CategoryNotFoundException;
import petly.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> all() {
        return this.categoriaRepository.findAll();
    }

    public Categoria save(Categoria categoria) {
        return this.categoriaRepository.save(categoria);
    }

    public Categoria one(Long id) {
        return this.categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public Categoria replace(Long id, Categoria categoria) {
        return this.categoriaRepository.findById(id).map(c -> (id.equals(categoria.getId()) ?
                        this.categoriaRepository.save(categoria) : null))
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    public void delete(Long id) {
        this.categoriaRepository.findById(id).map(c -> {
                    this.categoriaRepository.delete(c);
                    return c;
                })
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}