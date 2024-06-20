package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}
