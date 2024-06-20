package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.ExistenciasRepository;
import issste.gob.mx.SISADI.model.dao.TipoInsumoRepository;
import issste.gob.mx.SISADI.model.dto.ExistenciasDto;
import issste.gob.mx.SISADI.model.entity.Existencias;
import issste.gob.mx.SISADI.model.entity.TipoInsumo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class ExistenciasService {
    private final ExistenciasRepository repository;
    private final TipoInsumoRepository tipoInsumoRepository;

    public ExistenciasService(ExistenciasRepository repository, TipoInsumoRepository tipoInsumoRepository) {
        this.repository = repository;
        this.tipoInsumoRepository = tipoInsumoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll () {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById (Long id) {
        Optional<Existencias> foundExistencia = repository.findById(id);
        if (foundExistencia.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundExistencia, HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "ExistenciaNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (ExistenciasDto existenciasDto) {
        TipoInsumo foundTipoInsumo = tipoInsumoRepository.findById(existenciasDto.getTipoInsumo_id()).orElseThrow(() -> new RuntimeException("TipoInsumoNotFound"));

        Existencias existencias = new Existencias();
        existencias.setId_existencias(existenciasDto.getId_existencias());
        existencias.setPrecio(existenciasDto.getPrecio());
        existencias.setExistencia(existenciasDto.getExistencia());
        existencias.setImporte(existenciasDto.getImporte());
        existencias.setTipoInsumo(foundTipoInsumo);

        repository.save(existencias);

        return new ResponseEntity<>(new ApiResponse(existencias, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (ExistenciasDto existenciasDto) {
        Existencias foundExistencia = repository.findById(existenciasDto.getId_existencias()).orElseThrow(() -> new RuntimeException("ExistenciasNotFound"));
        TipoInsumo foundTipoInsumo = tipoInsumoRepository.findById(existenciasDto.getTipoInsumo_id()).orElseThrow(() -> new RuntimeException("TipoInsumoNotFound"));

        foundExistencia.setId_existencias(existenciasDto.getId_existencias());
        foundExistencia.setImporte(existenciasDto.getImporte());
        foundExistencia.setExistencia(existenciasDto.getExistencia());
        foundExistencia.setPrecio(existenciasDto.getPrecio());
        foundExistencia.setTipoInsumo(foundTipoInsumo);

        repository.saveAndFlush(foundExistencia);

        return new ResponseEntity<>(new ApiResponse(foundExistencia, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete (Long id) {
        Optional<Existencias> foundExistencias = repository.findById(id);
        if (foundExistencias.isPresent()){
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("ExistenciasNotFound");
        }
    }
}
