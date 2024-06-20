package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Salidas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalidasRepository extends JpaRepository<Salidas, Long> {

}
