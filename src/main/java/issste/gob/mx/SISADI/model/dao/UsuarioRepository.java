package issste.gob.mx.SISADI.model.dao;

import issste.gob.mx.SISADI.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Modifying
    @Query(value = "INSERT INTO usuario (id_usuario, usuario, contrasena) VALUES (:id_usuario, :usuario, :contrasena)", nativeQuery = true)
    int saveUsuario(@Param("id_usuario") Long id_usuario, @Param("usuario") String usuario, @Param("contrasena") String contrasena);

    Optional<Usuario> findByUsuario(String ussername);
}
