package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.InsumoRepository;
import issste.gob.mx.SISADI.model.dao.UsuarioRepository;
import issste.gob.mx.SISADI.model.dto.InsumoDto;
import issste.gob.mx.SISADI.model.entity.Insumo;
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
public class InsumoService {
    private final InsumoRepository repository;
    private final UsuarioRepository usuarioRepository;

    public InsumoService(InsumoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll () {
        return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById (Long id) {
        Optional<Insumo> foundInsumo = repository.findById(id);
        if (foundInsumo.isPresent()) {
            return new ResponseEntity<>(new ApiResponse(foundInsumo.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "InsumoNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> register (InsumoDto insumoDto) {
        Usuario usuario = usuarioRepository.findById(insumoDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        Insumo insumo = new Insumo();
        insumo.setClave(insumoDto.getClave());
        insumo.setDescripcion(insumoDto.getDescripcion());
        insumo.setUnidad(insumoDto.getUnidad());
        insumo.setUsuario(usuario);

        repository.save(insumo);

        return new ResponseEntity<>(new ApiResponse(insumo, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update (InsumoDto insumoDto) {
        Insumo foundInsumo = repository.findById(insumoDto.getId_insumo()).orElseThrow(() -> new RuntimeException("InsumoNotFound"));
        Usuario foundUsuario = usuarioRepository.findById(insumoDto.getUsuario_id()).orElseThrow(() -> new RuntimeException("UsuarioNotFound"));

        foundInsumo.setId_insumo(insumoDto.getId_insumo());
        foundInsumo.setClave(insumoDto.getClave());
        foundInsumo.setDescripcion(insumoDto.getDescripcion());
        foundInsumo.setUnidad(insumoDto.getUnidad());
        foundInsumo.setUsuario(foundUsuario);

        repository.save(foundInsumo);

        return new ResponseEntity<>(new ApiResponse(foundInsumo, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete (Long id) {
        Optional<Insumo> foundInsumo = repository.findById(id);
        if (foundInsumo.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("InsumoNotFound");
        }
    }
}
