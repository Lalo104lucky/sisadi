package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.ProveedorRepository;
import issste.gob.mx.SISADI.model.dao.UsuarioRepository;
import issste.gob.mx.SISADI.model.dto.ProveedorDto;
import issste.gob.mx.SISADI.model.entity.Proveedor;
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
public class ProveedorService {
    private final ProveedorRepository repository;
    private final UsuarioRepository usuarioRepository;


    public ProveedorService(ProveedorRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll() {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById (Long id) {
        Optional<Proveedor> foundProveedor = repository.findById(id);
        if (foundProveedor.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundProveedor.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "ProveedorNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (ProveedorDto proveedorDto) {
        Usuario usuario = usuarioRepository.findById(proveedorDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        Proveedor proveedor = new Proveedor();
        proveedor.setId_proveedor(proveedorDto.getId_proveedor());
        proveedor.setNombre(proveedorDto.getNombre());
        proveedor.setRfc(proveedorDto.getRfc());
        proveedor.setUsuario(usuario);

        repository.save(proveedor);

        return new ResponseEntity<>(new ApiResponse(proveedor, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (ProveedorDto proveedorDto) {
        Proveedor foundProveedor = repository.findById(proveedorDto.getId_proveedor()).orElseThrow(() -> new RuntimeException("ProveedorNotFound"));
        Usuario foundusuario = usuarioRepository.findById(proveedorDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        foundProveedor.setId_proveedor(proveedorDto.getId_proveedor());
        foundProveedor.setNombre(proveedorDto.getNombre());
        foundProveedor.setRfc(proveedorDto.getRfc());
        foundProveedor.setUsuario(foundusuario);

        repository.saveAndFlush(foundProveedor);

        return new ResponseEntity<>(new ApiResponse(foundProveedor, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete(Long id) {
        Optional<Proveedor> foundProveedor = repository.findById(id);
        if (foundProveedor.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("ProveedorNotFound");
        }
    }
}
