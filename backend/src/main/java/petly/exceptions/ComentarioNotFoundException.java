package petly.exceptions;

public class ComentarioNotFoundException extends RuntimeException {
    public ComentarioNotFoundException(Long id) {
        super("No se pudo encontrar el comentario con id " + id);
    }
}
