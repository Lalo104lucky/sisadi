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

    @Query(value = "SELECT i.id_insumo, i.clave, i.descripcion, i.precio, i.usuario_id FROM Insumo i WHERE i.clave = :clave", nativeQuery = true)
    List<Object[]> findByClave(@Param("clave") String clave);

    @Query(value = "SELECT i.id_insumo, e.id_entradas, s.id_salidas FROM insumo i " +
            "LEFT JOIN insumo_has_entradas ie ON i.id_insumo = ie.insumos_id " +
            "LEFT JOIN entradas e ON ie.entradas_id = e.id_entradas " +
            "LEFT JOIN insumo_has_salidas id ON i.id_insumo = id.insumos_id " +
            "LEFT JOIN salidas s ON id.salidas_id = s.id_salidas " +
            "WHERE i.id_insumo = :idInsumo", nativeQuery = true)
    List<Object[]> findInsumoByEntradasAndSalidas(@Param("idInsumo") Long idInsumo);
}
