package issste.gob.mx.SISADI.config;

import issste.gob.mx.SISADI.model.dao.PersonaRepository;
import issste.gob.mx.SISADI.model.dao.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Configuration
@RequiredArgsConstructor
public class InitialConfig implements CommandLineRunner {
    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional(rollbackFor = {SQLException.class})
    public void run(String... args) throws Exception {
        if(!usuarioRepository.existsById(1L)) {
            usuarioRepository.saveUsuario(1L, "torres_j_b", encoder.encode("123456"));
        }

        if(!usuarioRepository.existsById(2L)){
            usuarioRepository.saveUsuario(2L, "Lalo104lucky", encoder.encode("JaimezFlores104"));
        }

        if(!personaRepository.existsById(1L)){
            personaRepository.savePerson(1L, "JOEL", "TORRES", "BARRERA", 1L);
        }

        if(!personaRepository.existsById(2L)){
            personaRepository.savePerson(2L, "DIEGO EDUARDO", "JAIMEZ", "FLORES", 2L);
        }
    }


}
