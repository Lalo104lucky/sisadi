package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.InsumoRepository;
import issste.gob.mx.SISADI.model.dao.OperacionRepository;
import issste.gob.mx.SISADI.model.dao.SalidasRepository;
import issste.gob.mx.SISADI.model.dto.SalidasDto;
import issste.gob.mx.SISADI.model.entity.Insumo;
import issste.gob.mx.SISADI.model.entity.Operacion;
import issste.gob.mx.SISADI.model.entity.Salidas;
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
    private final OperacionRepository operacionRepository;
    private final InsumoRepository insumoRepository;

    public SalidasService(SalidasRepository repository, OperacionRepository operacionRepository, InsumoRepository insumoRepository) {
        this.repository = repository;
        this.operacionRepository = operacionRepository;
        this.insumoRepository = insumoRepository;
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
        Operacion operacion = operacionRepository.findById(salidasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));
        Insumo insumo = insumoRepository.findById(salidasDto.getInsumos_id()).orElseThrow(() -> new RuntimeException("InsumoNotFound"));

        Salidas salidas = new Salidas();
        salidas.setCantidad(salidasDto.getCantidad());
        salidas.setTotal(salidasDto.getTotal());
        salidas.setOperacion(operacion);
        salidas.setInsumo(insumo);

        repository.save(salidas);

        return new ResponseEntity<>(new ApiResponse(salidas, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update(SalidasDto salidasDto){
        Salidas foundSalidas = repository.findById(salidasDto.getId_salidas()).orElseThrow(() -> new RuntimeException("SalidasNotFound"));
        Operacion foundOperacion = operacionRepository.findById(salidasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));
        Insumo foundInsumo = insumoRepository.findById(salidasDto.getInsumos_id()).orElseThrow(() -> new RuntimeException("InsumoNotFound"));

        foundSalidas.setId_salidas(salidasDto.getId_salidas());
        foundSalidas.setCantidad(salidasDto.getCantidad());
        foundSalidas.setTotal(salidasDto.getTotal());
        foundSalidas.setOperacion(foundOperacion);
        foundSalidas.setInsumo(foundInsumo);

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
