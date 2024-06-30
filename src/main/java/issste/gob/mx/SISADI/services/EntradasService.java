package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.EntradasRepository;
import issste.gob.mx.SISADI.model.dao.InsumoRepository;
import issste.gob.mx.SISADI.model.dao.OperacionRepository;
import issste.gob.mx.SISADI.model.dto.EntradasDto;
import issste.gob.mx.SISADI.model.entity.Entradas;
import issste.gob.mx.SISADI.model.entity.Insumo;
import issste.gob.mx.SISADI.model.entity.Operacion;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class EntradasService {
    private final EntradasRepository repository;
    private final OperacionRepository operacionRepository;
    private final InsumoRepository insumoRepository;

    public EntradasService(EntradasRepository repository, OperacionRepository operacionRepository, InsumoRepository insumoRepository) {
        this.repository = repository;
        this.operacionRepository = operacionRepository;
        this.insumoRepository = insumoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll () {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById (Long id) {
        Optional<Entradas> foundEntradas = repository.findById(id);
        if (foundEntradas.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundEntradas.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "EntradasNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (EntradasDto entradasDto) {
        Operacion operacion = operacionRepository.findById(entradasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));
        Insumo insumo = insumoRepository.findById(entradasDto.getInsumos_id()).orElseThrow(() -> new RuntimeException("InsumoNotFound"));

        Entradas entradas = new Entradas();
        entradas.setCantidad(entradasDto.getCantidad());
        entradas.setTotal(entradasDto.getTotal());
        entradas.setOperacion(operacion);
        entradas.setInsumo(insumo);

        repository.save(entradas);

        return new ResponseEntity<>(new ApiResponse(entradas, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update(EntradasDto entradasDto) {
        Entradas foundEntradas = repository.findById(entradasDto.getId_entradas()).orElseThrow(() -> new RuntimeException("EntradasNotFound"));
        Operacion foundOperacion = operacionRepository.findById(entradasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));
        Insumo foundInsumo = insumoRepository.findById(entradasDto.getInsumos_id()).orElseThrow(() -> new RuntimeException("InsumoNotFound"));

        foundEntradas.setId_entradas(entradasDto.getId_entradas());
        foundEntradas.setCantidad(entradasDto.getCantidad());
        foundEntradas.setTotal(entradasDto.getTotal());
        foundEntradas.setOperacion(foundOperacion);
        foundEntradas.setInsumo(foundInsumo);

        repository.saveAndFlush(foundEntradas);

        return new ResponseEntity<>(new ApiResponse(foundEntradas, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor =  {SQLException.class})
    public void delete(Long id) {
        Optional<Entradas> foundEntradas = repository.findById(id);
        if (foundEntradas.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("EntradasNotFound");
        }
    }
}
