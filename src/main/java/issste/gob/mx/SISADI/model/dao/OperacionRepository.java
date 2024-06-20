package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Operacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperacionRepository extends JpaRepository<Operacion, Long> {
}
