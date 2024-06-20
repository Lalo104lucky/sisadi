package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.EntradasRepository;
import issste.gob.mx.SISADI.model.dao.OperacionRepository;
import issste.gob.mx.SISADI.model.dao.ProveedorRepository;
import issste.gob.mx.SISADI.model.dao.TipoInsumoRepository;
import issste.gob.mx.SISADI.model.dto.EntradasDto;
import issste.gob.mx.SISADI.model.entity.Entradas;
import issste.gob.mx.SISADI.model.entity.Operacion;
import issste.gob.mx.SISADI.model.entity.Proveedor;
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
public class EntradasService {
    private final EntradasRepository repository;
    private final OperacionRepository operacionRepository;
    private final TipoInsumoRepository tipoInsumoRepository;
    private final ProveedorRepository proveedorRepository;

    public EntradasService(EntradasRepository repository, OperacionRepository operacionRepository, TipoInsumoRepository tipoInsumoRepository, ProveedorRepository proveedorRepository) {
        this.repository = repository;
        this.operacionRepository = operacionRepository;
        this.tipoInsumoRepository = tipoInsumoRepository;
        this.proveedorRepository = proveedorRepository;
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
        TipoInsumo tipoInsumo = tipoInsumoRepository.findById(entradasDto.getTipoInsumo_id()).orElseThrow(() -> new RuntimeException("TipoInsumoNotFound"));
        Proveedor proveedor = proveedorRepository.findById(entradasDto.getProveedor_id()).orElseThrow(() -> new RuntimeException("ProveedorNotFound"));

        Entradas entradas = new Entradas();
        entradas.setId_entradas(entradasDto.getId_entradas());
        entradas.setCantidad(entradasDto.getCantidad());
        entradas.setOperacion(operacion);
        entradas.setTipoInsumo(tipoInsumo);
        entradas.setProveedor(proveedor);

        repository.save(entradas);

        return new ResponseEntity<>(new ApiResponse(entradas, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update(EntradasDto entradasDto) {
        Entradas foundEntradas = repository.findById(entradasDto.getId_entradas()).orElseThrow(() -> new RuntimeException("EntradasNotFound"));
        Operacion foundOperacion = operacionRepository.findById(entradasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));
        TipoInsumo foundTipoInsumo = tipoInsumoRepository.findById(entradasDto.getTipoInsumo_id()).orElseThrow(() -> new RuntimeException("TipoInsumoNotFound"));
        Proveedor foundProveedor = proveedorRepository.findById(entradasDto.getProveedor_id()).orElseThrow(() -> new RuntimeException("ProveedorNotFound"));

        foundEntradas.setId_entradas(entradasDto.getId_entradas());
        foundEntradas.setCantidad(entradasDto.getCantidad());
        foundEntradas.setOperacion(foundOperacion);
        foundEntradas.setTipoInsumo(foundTipoInsumo);
        foundEntradas.setProveedor(foundProveedor);

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
