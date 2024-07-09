package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.*;
import issste.gob.mx.SISADI.model.dto.ExistenciasDto;
import issste.gob.mx.SISADI.model.entity.*;
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
    private final SalidasRepository salidasRepository;
    private final EntradasRepository entradasRepository;

    public ExistenciasService(ExistenciasRepository repository, SalidasRepository salidasRepository, EntradasRepository entradasRepository) {
        this.repository = repository;
        this.salidasRepository = salidasRepository;
        this.entradasRepository = entradasRepository;
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
    public ResponseEntity<ApiResponse> register(ExistenciasDto existenciasDto) {
        Existencias existencias = new Existencias();
        existencias.setCantidad(0L);
        existencias.setTotal(0.0);

        if (existenciasDto.getEntradas_id() != null) {
            Entradas entradas = entradasRepository.findById(existenciasDto.getEntradas_id())
                    .orElseThrow(() -> new RuntimeException("EntradasNotFound"));
            existencias.setCantidad(existencias.getCantidad() + entradas.getCantidad());
            existencias.setTotal(existencias.getTotal() + entradas.getTotal());
            existencias.setEntradas(entradas);
        }

        if (existenciasDto.getSalidas_id() != null) {
            Salidas salidas = salidasRepository.findById(existenciasDto.getSalidas_id())
                    .orElseThrow(() -> new RuntimeException("SalidasNotFound"));
            existencias.setCantidad(existencias.getCantidad() - salidas.getCantidad());
            existencias.setTotal(existencias.getTotal() - salidas.getTotal());
            existencias.setSalidas(salidas);
        }

        repository.save(existencias);
        return new ResponseEntity<>(new ApiResponse(existencias, HttpStatus.OK), HttpStatus.OK);
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (ExistenciasDto existenciasDto) {
        Existencias foundExistencia = repository.findById(existenciasDto.getId_existencias()).orElseThrow(() -> new RuntimeException("ExistenciasNotFound"));

        foundExistencia.setId_existencias(existenciasDto.getId_existencias());
        foundExistencia.setCantidad(existenciasDto.getCantidad());
        foundExistencia.setTotal(existenciasDto.getTotal());

        repository.saveAndFlush(foundExistencia);

        return new ResponseEntity<>(new ApiResponse(foundExistencia, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete(Long id) {
        Optional<Existencias> foundExistencias = repository.findById(id);
        Existencias existencias = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExistenciasNotFound"));
        existencias.setEntradas(null);
        existencias.setSalidas(null);
        if (foundExistencias.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("InsumoNotFound");
        }
    }
}
