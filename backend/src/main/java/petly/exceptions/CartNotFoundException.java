package petly.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long id) {
        super("No se ha encontrado el carrito con id: " + id);
    }
}