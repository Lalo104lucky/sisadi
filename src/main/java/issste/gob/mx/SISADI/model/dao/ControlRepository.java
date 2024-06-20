package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Control;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlRepository extends JpaRepository<Control, Long> {

}
