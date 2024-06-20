package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.TipoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoInsumoRepository extends JpaRepository<TipoInsumo, Long> {

}
