package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.PersonaRepository;
import issste.gob.mx.SISADI.model.dao.UsuarioRepository;
import issste.gob.mx.SISADI.model.dto.PersonaDto;
import issste.gob.mx.SISADI.model.entity.Persona;
import issste.gob.mx.SISADI.model.entity.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class PersonaService {
    private final PersonaRepository repository;
    private final UsuarioRepository usuarioRepository;

    public PersonaService(PersonaRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById (Long id) {
        Optional<Persona> foundPersona = repository.findById(id);
        if (foundPersona.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundPersona.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "PersonaNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (PersonaDto personaDto) {
        Usuario usuario = usuarioRepository.findById(personaDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        Persona persona = new Persona();
        persona.setId_persona(personaDto.getId_persona());
        persona.setNombre(personaDto.getNombre());
        persona.setApellido_p(personaDto.getApellido_p());
        persona.setApellido_m(personaDto.getApellido_m());
        persona.setUsuario(usuario);

        repository.save(persona);

        return new ResponseEntity<>(new ApiResponse(persona, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (PersonaDto personaDto) {
        Persona foundPersona = repository.findById(personaDto.getId_persona()).orElseThrow(() -> new RuntimeException("PersonaNotFound"));
        Usuario foundUsuario = usuarioRepository.findById(personaDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        foundPersona.setId_persona(personaDto.getId_persona());
        foundPersona.setNombre(personaDto.getNombre());
        foundPersona.setApellido_p(personaDto.getApellido_p());
        foundPersona.setApellido_m(personaDto.getApellido_m());
        foundPersona.setUsuario(foundUsuario);

        repository.saveAndFlush(foundPersona);

        return new ResponseEntity<>(new ApiResponse(foundPersona, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete (Long id) {
        Optional<Persona> foundPersona = repository.findById(id);
        if (foundPersona.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("PersonaNotFound");
        }
    }
}
