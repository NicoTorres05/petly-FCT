package petly.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import petly.dto.CategoriaDTO;
import petly.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT new petly.dto.CategoriaDTO(" +
            "c.id, c.nombre, c.descripcion, COUNT(p)) " +
            "FROM Categoria c LEFT JOIN c.productos p " +
            "GROUP BY c.id, c.nombre, c.descripcion")
    List<CategoriaDTO> findAllWithProductCount();

    @Query("SELECT new petly.dto.CategoriaDTO(" +
            "c.id, c.nombre, c.descripcion, COUNT(p)) " +
            "FROM Categoria c LEFT JOIN c.productos p " +
            "WHERE c.id = :id " +
            "GROUP BY c.id, c.nombre, c.descripcion")
    Optional<CategoriaDTO> findOneWithProductCount(@Param("id") Long id);

}