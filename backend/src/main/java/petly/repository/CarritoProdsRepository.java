package petly.repository;

import petly.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import petly.model.CarritoProds;

@Repository
public interface CarritoProdsRepository extends JpaRepository<CarritoProds, Long> {
}