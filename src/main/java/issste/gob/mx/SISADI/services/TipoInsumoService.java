package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.InsumoRepository;
import issste.gob.mx.SISADI.model.dao.TipoInsumoRepository;
import issste.gob.mx.SISADI.model.dto.TipoInsumoDto;
import issste.gob.mx.SISADI.model.entity.Insumo;
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
public class TipoInsumoService {
    private final TipoInsumoRepository repository;
    private final InsumoRepository insumoRepository;

    public TipoInsumoService(TipoInsumoRepository repository, InsumoRepository insumoRepository) {
        this.repository = repository;
        this.insumoRepository = insumoRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id){
        Optional<TipoInsumo> foundTipoInsumo = repository.findById(id);
        if (foundTipoInsumo.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundTipoInsumo.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "TipoInsumoNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (TipoInsumoDto tipoInsumoDto) {
        Insumo insumo = insumoRepository.findById(tipoInsumoDto.getInsumo_id()).orElseThrow(() -> new RuntimeException("InsumoNotFound"));

        TipoInsumo tipoInsumo = new TipoInsumo();
        tipoInsumo.setId_tipoinsumo(tipoInsumoDto.getId_tipoinsumo());
        tipoInsumo.setNombre(tipoInsumoDto.getNombre());
        tipoInsumo.setPartida(tipoInsumoDto.getPartida());
        tipoInsumo.setInsumo(insumo);

        repository.save(tipoInsumo);

        return new ResponseEntity<>(new ApiResponse(tipoInsumo, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update(TipoInsumoDto tipoInsumoDto){
        TipoInsumo foundTipoInsumo = repository.findById(tipoInsumoDto.getId_tipoinsumo()).orElseThrow(() -> new RuntimeException("TipoInsumoNotFound"));
        Insumo foundInsumo = insumoRepository.findById(tipoInsumoDto.getInsumo_id()).orElseThrow(() -> new RuntimeException("InsumoNotFound"));

        foundTipoInsumo.setId_tipoinsumo(tipoInsumoDto.getId_tipoinsumo());
        foundTipoInsumo.setNombre(tipoInsumoDto.getNombre());
        foundTipoInsumo.setPartida(tipoInsumoDto.getPartida());
        foundTipoInsumo.setInsumo(foundInsumo);

        repository.saveAndFlush(foundTipoInsumo);

        return new ResponseEntity<>(new ApiResponse(foundTipoInsumo, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete(Long id){
        Optional<TipoInsumo> foundTipoInsumo = repository.findById(id);
        if (foundTipoInsumo.isPresent()){
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("TipoInsumoNotFound");
        }
    }
}

