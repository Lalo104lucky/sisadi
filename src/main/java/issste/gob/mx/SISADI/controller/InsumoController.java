package issste.gob.mx.SISADI.controller;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dto.InsumoDto;
import issste.gob.mx.SISADI.services.InsumoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sisadi/insumo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class InsumoController {
    private final InsumoService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll () {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> save (@RequestBody InsumoDto insumoDto) {
        return service.register(insumoDto);
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse> update (@RequestBody InsumoDto insumoDto) {
        try {
            service.update(insumoDto);
            return new ResponseEntity<>(new ApiResponse(insumoDto, HttpStatus.OK), HttpStatus.OK);
        } catch (RuntimeException runtimeException) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, runtimeException.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete (@PathVariable("id") Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(new ApiResponse(id, HttpStatus.OK), HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, true, exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}