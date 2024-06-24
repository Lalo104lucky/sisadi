package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.ControlRepository;
import issste.gob.mx.SISADI.model.dao.EntradasRepository;
import issste.gob.mx.SISADI.model.dao.SalidasRepository;
import issste.gob.mx.SISADI.model.dto.ControlDto;
import issste.gob.mx.SISADI.model.entity.Control;
import issste.gob.mx.SISADI.model.entity.Entradas;
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
public class ControlService {
    private final ControlRepository repository;
    private final EntradasRepository entradasRepository;
    private final SalidasRepository salidasRepository;

    public ControlService(ControlRepository repository, EntradasRepository entradasRepository, SalidasRepository salidasRepository) {
        this.repository = repository;
        this.entradasRepository = entradasRepository;
        this.salidasRepository = salidasRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll () {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById (Long id) {
        Optional<Control> foundControl = repository.findById(id);
        if (foundControl.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundControl.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "ControlNotfound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (ControlDto controlDto) {
        Entradas entradas = entradasRepository.findById(controlDto.getEntradas_id()).orElseThrow(() -> new RuntimeException("EntradasNotFound"));
        Salidas salidas = salidasRepository.findById(controlDto.getSalidas_id()).orElseThrow(() -> new RuntimeException("SalidasNotFound"));

        Control control = new Control();
        control.setId_control(controlDto.getId_control());
        control.setImp_entradas(controlDto.getImp_entradas());
        control.setImp_inv_inicial(controlDto.getImp_inv_inicial());
        control.setImp_inv_final(controlDto.getImp_inv_final());
        control.setImp_salidas(controlDto.getImp_salidas());
        control.setPrecio_final(controlDto.getPrecio_final());
        control.setEntradas(entradas);
        control.setSalidas(salidas);

        repository.save(control);

        return new ResponseEntity<>(new ApiResponse(control, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (ControlDto controlDto) {
        Control foundControl = repository.findById(controlDto.getId_control()).orElseThrow(() -> new RuntimeException("ControlNotFound"));
        Entradas foundEntradas = entradasRepository.findById(controlDto.getEntradas_id()).orElseThrow(() -> new RuntimeException("EntradasNotFound"));
        Salidas foundSalidas = salidasRepository.findById(controlDto.getSalidas_id()).orElseThrow(() -> new RuntimeException("SalidasNotFound"));

        foundControl.setId_control(controlDto.getId_control());
        foundControl.setImp_entradas(controlDto.getImp_entradas());
        foundControl.setImp_inv_inicial(controlDto.getImp_inv_inicial());
        foundControl.setImp_inv_final(controlDto.getImp_inv_final());
        foundControl.setImp_salidas(controlDto.getImp_salidas());
        foundControl.setPrecio_final(controlDto.getPrecio_final());
        foundControl.setEntradas(foundEntradas);
        foundControl.setSalidas(foundSalidas);

        repository.saveAndFlush(foundControl);

        return new ResponseEntity<>(new ApiResponse(foundControl, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete (Long id) {
        Optional<Control> foundControl = repository.findById(id);
        if (foundControl.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("ControlNotFound");
        }
    }
}