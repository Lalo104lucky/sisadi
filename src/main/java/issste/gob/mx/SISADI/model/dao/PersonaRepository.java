package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    @Modifying
    @Query(value = "INSERT INTO persona (id_persona, nombre, apellido_p, apellido_m, usuario_id) VALUES (:id_persona, :nombre, :apellido_p, :apellido_m, :usuario_id)", nativeQuery = true)
    int savePerson(@Param("id_persona") Long id_persona, @Param("nombre") String nombre, @Param("apellido_p") String apellido_p, @Param("apellido_m") String apellido_m, @Param("usuario_id") Long usuario_id);
}
