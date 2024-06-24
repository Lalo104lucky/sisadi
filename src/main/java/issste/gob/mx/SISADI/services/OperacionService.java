package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.*;
import issste.gob.mx.SISADI.model.dto.OperacionDto;
import issste.gob.mx.SISADI.model.entity.Existencias;
import issste.gob.mx.SISADI.model.entity.Operacion;
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
public class OperacionService {
    private final OperacionRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ExistenciasRepository existenciasRepository;

    public OperacionService(OperacionRepository repository, UsuarioRepository usuarioRepository, ExistenciasRepository existenciasRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.existenciasRepository = existenciasRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById (Long id) {
        Optional<Operacion> foundOperacion = repository.findById(id);
        if (foundOperacion.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundOperacion, HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "OperacionNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (OperacionDto operacionDto) {
        Existencias foundExistencia = existenciasRepository.findById(operacionDto.getExistencias_id()).orElseThrow(() -> new RuntimeException("ExistenciasNotFound"));
        Usuario foundUsuario = usuarioRepository.findById(operacionDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        Operacion operacion = new Operacion();
        operacion.setId_operacion(operacionDto.getId_operacion());
        operacion.setNom_operacion(operacionDto.getNom_operacion());
        operacion.setFolio(operacionDto.getFolio());
        operacion.setFecha_inicial(operacionDto.getFecha_inicial());
        operacion.setFecha_final(operacionDto.getFecha_final());
        operacion.setUnidad(operacionDto.getUnidad());
        operacion.setUsuario(foundUsuario);
        operacion.setExistencias(foundExistencia);

        repository.save(operacion);

        return new ResponseEntity<>(new ApiResponse(operacion, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (OperacionDto operacionDto) {
        Operacion foundOperacion = repository.findById(operacionDto.getId_operacion()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));
        Existencias foundExistencia = existenciasRepository.findById(operacionDto.getExistencias_id()).orElseThrow(() -> new RuntimeException("ExistenciasNotFound"));
        Usuario foundUsuario = usuarioRepository.findById(operacionDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        foundOperacion.setId_operacion(operacionDto.getId_operacion());
        foundOperacion.setNom_operacion(operacionDto.getNom_operacion());
        foundOperacion.setFolio(operacionDto.getFolio());
        foundOperacion.setFecha_inicial(operacionDto.getFecha_inicial());
        foundOperacion.setFecha_final(operacionDto.getFecha_final());
        foundOperacion.setUnidad(operacionDto.getUnidad());
        foundOperacion.setUsuario(foundUsuario);
        foundOperacion.setExistencias(foundExistencia);

        repository.saveAndFlush(foundOperacion);

        return new ResponseEntity<>(new ApiResponse(foundOperacion, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete(Long id) {
        Optional<Operacion> foundOperacion = repository.findById(id);
        if (foundOperacion.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("OperacionNotFound");
        }
    }
}
