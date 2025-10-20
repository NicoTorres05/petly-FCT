package petly.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("No se ha encontrado la categoria con id: " + id);
    }
}