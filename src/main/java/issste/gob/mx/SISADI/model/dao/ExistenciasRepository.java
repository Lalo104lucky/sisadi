package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Existencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExistenciasRepository extends JpaRepository<Existencias, Long> {

}
