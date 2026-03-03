package petly.service;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import petly.dto.ProductoDTO;
import petly.dto.UsuarioRegDTO;
import petly.model.Categoria;
import petly.model.Producto;

import petly.exceptions.ProductNotFoundException;
import petly.model.Usuario;
import petly.repository.ProductoRepository;
import petly.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    private CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
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

    public String guardarFoto(Long id, MultipartFile file) throws IOException {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Path uploadDir = Paths.get("uploads/prods/");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalName = file.getOriginalFilename();
        assert originalName != null;
        String safeName = originalName.replaceAll(" ", "_");

        String filename = "product_" + id + "_" + safeName;

        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        producto.setFoto("/uploads/prods/" + filename);
        productoRepository.save(producto);

        return producto.getFoto();
    }

    public Producto create(ProductoDTO dto) {

        System.out.println(dto);

        Producto producto = new Producto();

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setDescuento(dto.getDescuento());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        producto.setFoto(dto.getFoto());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        return productoRepository.save(producto);
    }



}