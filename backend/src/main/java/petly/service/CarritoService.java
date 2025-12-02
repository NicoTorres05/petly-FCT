package petly.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import petly.dto.CarritoDTO;
import petly.dto.CarritoProdsDTO;
import petly.exceptions.CategoryNotFoundException;
import petly.model.Carrito;
import petly.model.CarritoProds;
import petly.model.Producto;
import petly.repository.CarritoProdsRepository;
import petly.repository.CarritoRepository;
import petly.repository.ProductoRepository;
import petly.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProdsRepository carritoProdsRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void addProducto(Long userId, Long productId, int cantidad) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(userId, Carrito.Estado.ACTIVO)
                .orElseGet(() -> crearNuevoCarrito(userId));


        if (carrito.getId() == null) {
            carrito = carritoRepository.save(carrito);
        }

        Producto producto = productoRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<CarritoProds> existingItem = carrito.getCarProductos().stream()
                .filter(item -> item.getProducto().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CarritoProds item = existingItem.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            CarritoProds item = new CarritoProds();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecio(producto.getPrecio());
            carrito.getCarProductos().add(item);
        }


        calcTotal(carrito);
    }


    private Carrito crearNuevoCarrito(Long userId) {
        System.out.println("Entrando a crearNuevoCarrito para userId = " + userId);

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

        System.out.println("Usuario encontrado: " + carrito.getUsuario().getId());

        carrito.setEstado(Carrito.Estado.ACTIVO);
        carrito.setPrecioTotal(BigDecimal.ZERO);

        Carrito savedCarrito = carritoRepository.save(carrito);
        System.out.println("Carrito creado con ID: " + savedCarrito.getId());

        return savedCarrito;
    }


    public List<Carrito> all() {
        return carritoRepository.findAll();
    }

    public Carrito one(Long id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    public List<Carrito> findByUsuarioId(Long userId) {
        return carritoRepository.findByUsuarioId(userId);
    }

    public Optional<Carrito> activeByUsuarioId(Long userId) {
        return carritoRepository.findByUsuarioIdAndEstado(userId, Carrito.Estado.ACTIVO);
    }

    public void delete(Long id) {
        carritoRepository.deleteById(id);

    }

    public void deleteItem(Long idUsuario, Long idItem) {
        carritoProdsRepository.deleteItemFromCarrito(idUsuario, idItem);

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(idUsuario, Carrito.Estado.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        calcTotal(carrito);
    }

    public void restarProducto(Long userId, Long productoId, int cantidad) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(userId, Carrito.Estado.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Carrito activo no encontrado"));

        CarritoProds item = carrito.findItem(productoId);

        if (item != null) {
            item.setCantidad(item.getCantidad() - cantidad);

            if (item.getCantidad() <= 0) {
                carrito.getCarProductos().remove(item);
            }

            carritoRepository.save(carrito);

        }
        calcTotal(carrito);
    }


    public Carrito replace(Long id, Carrito carrito) {
        carrito.setId(id);
        return carritoRepository.save(carrito);
    }

    public Carrito getCarritoActivo(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = auth.getName();
        Long userId = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getId();

        return carritoRepository.findByUsuarioIdAndEstado(userId, Carrito.Estado.ACTIVO)
                .orElseGet(() -> crearNuevoCarrito(userId));
    }

    @Transactional
    public Carrito completarCarrito(Long userId) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(userId, Carrito.Estado.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No hay carrito activo para este usuario"));

        carrito.setEstado(Carrito.Estado.COMPLETADO);

        return carritoRepository.save(carrito);
    }

    public CarritoDTO mapToDTO(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setPrecioTotal(carrito.getPrecioTotal());

        List<CarritoProdsDTO> productos = carrito.getCarProductos().stream()
                .map(item -> {
                    CarritoProdsDTO p = new CarritoProdsDTO();
                    p.setId(item.getId());
                    p.setProductoId(item.getProducto().getId());
                    p.setPrecio(item.getPrecio());
                    p.setCantidad(item.getCantidad());
                    return p;
                }).toList();

        dto.setProductos(productos);
        return dto;
    }

    public BigDecimal calcTotal(Carrito carrito) {
        BigDecimal total = carrito.getCarProductos().stream()
                .map(item -> item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        carrito.setPrecioTotal(total);
        carritoRepository.save(carrito);

        return total;
    }
}
