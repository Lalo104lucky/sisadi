package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.*;
import issste.gob.mx.SISADI.model.dto.OperacionDto;
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
public class OperacionService {
    private final OperacionRepository repository;
    private final ProveedorRepository proveedorRepository;

    public OperacionService(OperacionRepository repository, ProveedorRepository proveedorRepository) {
        this.repository = repository;
        this.proveedorRepository = proveedorRepository;
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

        Operacion operacion = new Operacion();
        operacion.setNombre(operacionDto.getNombre());
        operacion.setFolio(operacionDto.getFolio());
        operacion.setTipo_movimiento(operacionDto.getTipo_movimiento());
        operacion.setFecha(operacionDto.getFecha());
        operacion.setUnidad(operacionDto.getUnidad());
        if (operacionDto.getProveedor_id() != null) {
            Proveedor proveedor = proveedorRepository.findById(operacionDto.getProveedor_id())
                    .orElseThrow(() -> new RuntimeException("ProveedorNotFound"));
            operacion.setProveedor(proveedor);
        } else {
            operacion.setProveedor(null);
        }

        repository.save(operacion);

        return new ResponseEntity<>(new ApiResponse(operacion, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (OperacionDto operacionDto) {
        Operacion foundOperacion = repository.findById(operacionDto.getId_operacion()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));

        foundOperacion.setFolio(operacionDto.getFolio());
        foundOperacion.setTipo_movimiento(operacionDto.getTipo_movimiento());
        foundOperacion.setFecha(operacionDto.getFecha());
        foundOperacion.setUnidad(operacionDto.getUnidad());
        if (operacionDto.getProveedor_id() != null) {
            Proveedor foundProveedor = proveedorRepository.findById(operacionDto.getProveedor_id())
                    .orElseThrow(() -> new RuntimeException("ProveedorNotFound"));
            foundOperacion.setProveedor(foundProveedor);
        } else {
            foundOperacion.setProveedor(null);
        }

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
