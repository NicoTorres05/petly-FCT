package petly.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import petly.model.CarritoProds;

@Repository
public interface CarritoProdsRepository extends JpaRepository<CarritoProds, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CarritoProds cp WHERE cp.carrito.usuario.id = :idUsuario AND cp.producto.id = :idItem")
    void deleteItemFromCarrito(@Param("idUsuario") Long idUsuario, @Param("idItem") Long idItem);
}