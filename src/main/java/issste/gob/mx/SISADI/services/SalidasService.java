package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.OperacionRepository;
import issste.gob.mx.SISADI.model.dao.SalidasRepository;
import issste.gob.mx.SISADI.model.dao.TipoInsumoRepository;
import issste.gob.mx.SISADI.model.dto.SalidasDto;
import issste.gob.mx.SISADI.model.entity.Operacion;
import issste.gob.mx.SISADI.model.entity.Salidas;
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
public class SalidasService {
    private final SalidasRepository repository;
    private final TipoInsumoRepository tipoInsumoRepository;
    private final OperacionRepository operacionRepository;


    public SalidasService(SalidasRepository repository, TipoInsumoRepository tipoInsumoRepository, OperacionRepository operacionRepository) {
        this.repository = repository;
        this.tipoInsumoRepository = tipoInsumoRepository;
        this.operacionRepository = operacionRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id){
        Optional<Salidas> foundSalidas = repository.findById(id);
        if (foundSalidas.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundSalidas.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "SalidasNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register(SalidasDto salidasDto){
        TipoInsumo tipoInsumo = tipoInsumoRepository.findById(salidasDto.getTipoInsumo_id()).orElseThrow(() -> new RuntimeException("TipoInsumoNotFound"));
        Operacion operacion = operacionRepository.findById(salidasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));

        Salidas salidas = new Salidas();
        salidas.setId_salidas(salidasDto.getId_salidas());
        salidas.setCantidad(salidas.getCantidad());
        salidas.setTipoInsumo(tipoInsumo);
        salidas.setOperacion(operacion);

        repository.save(salidas);

        return new ResponseEntity<>(new ApiResponse(salidas, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update(SalidasDto salidasDto){
        Salidas foundSalidas = repository.findById(salidasDto.getId_salidas()).orElseThrow(() -> new RuntimeException("SalidasNotFound"));
        TipoInsumo foundtipoInsumo = tipoInsumoRepository.findById(salidasDto.getTipoInsumo_id()).orElseThrow(() -> new RuntimeException("TipoInsumoNotFound"));
        Operacion foundoperacion = operacionRepository.findById(salidasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));

        foundSalidas.setId_salidas(salidasDto.getId_salidas());
        foundSalidas.setCantidad(salidasDto.getCantidad());
        foundSalidas.setTipoInsumo(foundtipoInsumo);
        foundSalidas.setOperacion(foundoperacion);

        repository.saveAndFlush(foundSalidas);

        return new ResponseEntity<>(new ApiResponse(foundSalidas, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete(Long id) {
        Optional<Salidas> foundSalidas = repository.findById(id);
        if(foundSalidas.isPresent()){
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("SalidasNotFound");
        }
    }
}
