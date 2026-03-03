package petly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petly.model.Comentario;

@Repository

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}
