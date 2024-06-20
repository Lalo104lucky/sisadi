package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Entradas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradasRepository extends JpaRepository<Entradas, Long> {

}
