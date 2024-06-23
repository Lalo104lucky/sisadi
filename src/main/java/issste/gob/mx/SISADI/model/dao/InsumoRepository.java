package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    @Query(value = "SELECT i.id_insumo, i.clave, i.descripcion FROM Insumo i WHERE i.clave = :clave", nativeQuery = true)
    List<Object[]> findByClave(@Param("clave") String clave);

}
