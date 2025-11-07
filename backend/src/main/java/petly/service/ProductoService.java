package petly.service;

import petly.model.Producto;

import petly.exceptions.ProductNotFoundException;
import petly.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> all() {
        return this.productoRepository.findAll();
    }

    public Producto save(Producto product) {
        return this.productoRepository.save(product);
    }

    public Producto one(Long id) {
        return this.productoRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Producto replace(Long id, Producto product) {
        return this.productoRepository.findById(id).map(p -> (id.equals(product.getId()) ?
                        this.productoRepository.save(product) : null))
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void delete(Long id) {
        this.productoRepository.findById(id).map(p -> {
                    this.productoRepository.delete(p);
                    return p;
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Producto> findByCategoriaId(Long id) {
        return productoRepository.findByCategoriaId(id);
    }
}