package petly.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import petly.dto.CheckoutMessage;
import petly.model.Carrito;
import petly.model.CarritoProds;
import petly.model.Producto;
import petly.model.Usuario;
import petly.repository.CarritoRepository;
import petly.repository.CategoriaRepository;
import petly.repository.ProductoRepository;
import petly.repository.UsuarioRepository;

import java.math.BigDecimal;

@Service
public class CheckoutService {
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final CarritoService carritoService;

    public CheckoutService(
            UsuarioRepository usuarioRepository,
            CarritoRepository carritoRepository,
            ProductoRepository productoRepository,
            CarritoService carritoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
        this.carritoService = carritoService;
    }

    @Transactional
    public CheckoutMessage checkout(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuario.getId(), Carrito.Estado.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getCarProductos().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // 1️⃣ Validar stock
        for (CarritoProds prod : carrito.getCarProductos()) {
            int cant = prod.getCantidad();
            int stock = prod.getProducto().getStock();

            if (cant > stock) {
                throw new RuntimeException(
                        "Stock insuficiente para " + prod.getProducto().getNombre()
                );
            }
        }

        // 2️⃣ Descontar stock
        for (CarritoProds prod : carrito.getCarProductos()) {
            Producto producto = prod.getProducto();
            producto.setStock(producto.getStock() - prod.getCantidad());
            productoRepository.save(producto);
        }

        // 3️⃣ Calcular total y validar saldo
        BigDecimal total = carritoService.calcTotal(carrito);
        BigDecimal saldo = usuario.getSaldo();

        if (saldo.compareTo(total) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // 4️⃣ Descontar saldo
        usuario.setSaldo(saldo.subtract(total));
        usuarioRepository.save(usuario);

        // 5️⃣ Completar carrito
        carritoService.completarCarrito(usuario.getId());

        return new CheckoutMessage(
                "Carrito completado correctamente",
                usuario.getSaldo()
        );
    }
}
