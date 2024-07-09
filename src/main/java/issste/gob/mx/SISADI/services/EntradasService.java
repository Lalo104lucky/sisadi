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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public ResponseEntity<ApiResponse> register(EntradasDto entradasDto) {
        Operacion operacion = operacionRepository.findById(entradasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));

        Entradas entradas = new Entradas();
        entradas.setCantidad(entradasDto.getCantidad());
        entradas.setTotal(entradasDto.getTotal());
        entradas.setOperacion(operacion);
        Set<Insumo> insumos = new HashSet<>();
        Set<Entradas> entradasSet = new HashSet<>();
        entradasSet.add(entradas);

        List<Insumo> insumoList = insumoRepository.findAllById(entradasDto.getInsumos_id());
        for (Insumo insumo : insumoList) {
            Set<Entradas> entradasActuales = insumo.getEntradas();
            if (entradasActuales != null) {
                entradasActuales = new HashSet<>(entradasActuales);
            } else {
                entradasActuales = new HashSet<>();
            }
            entradasActuales.add(entradas);
            insumo.setEntradas(entradasActuales);
            insumos.add(insumo);
        }
        entradas.setInsumos(insumos);

        repository.save(entradas);

        return new ResponseEntity<>(new ApiResponse(entradas, HttpStatus.OK), HttpStatus.OK);
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> update(EntradasDto entradasDto) {
        Entradas foundEntradas = repository.findById(entradasDto.getId_entradas()).orElseThrow(() -> new RuntimeException("EntradasNotFound"));
        Operacion foundOperacion = operacionRepository.findById(entradasDto.getOperacion_id()).orElseThrow(() -> new RuntimeException("OperacionNotFound"));

        foundEntradas.setId_entradas(entradasDto.getId_entradas());
        foundEntradas.setCantidad(entradasDto.getCantidad());
        foundEntradas.setTotal(entradasDto.getTotal());
        foundEntradas.setOperacion(foundOperacion);
        Set<Insumo> insumos = new HashSet<>();
        Set<Entradas> entradas1 = new HashSet<>();
        entradas1.add(foundEntradas);
        insumoRepository.findAllById(entradasDto.getInsumos_id()).forEach(insumo -> {
            insumo.setEntradas(entradas1);
            insumos.add(insumo);
        });
        foundEntradas.setInsumos(insumos);

        repository.saveAndFlush(foundEntradas);

        return new ResponseEntity<>(new ApiResponse(foundEntradas, HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void delete(Long id) {
        Optional<Entradas> optionalEntrada = repository.findById(id);
        if (optionalEntrada.isPresent()) {
            Entradas entrada = optionalEntrada.get();

            if (entrada.getInsumos() != null) {
                for (Insumo insumo : entrada.getInsumos()) {
                    if (insumo != null) {
                        insumo.getEntradas().remove(entrada);
                    }
                }
            }

            if (entrada.getInsumos() != null) {
                insumoRepository.saveAll(entrada.getInsumos());
            }

            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException("EntradasNotFound");
        }
    }



}
