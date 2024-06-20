package issste.gob.mx.SISADI.controller;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dto.EntradasDto;
import issste.gob.mx.SISADI.services.EntradasService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sisadi/entradas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class EntradasController {

    private final EntradasService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById (@PathVariable("id") Long id){
        return service.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> save (@RequestBody EntradasDto entradasDto){
        return service.register(entradasDto);
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse> update (@RequestBody EntradasDto entradasDto){
        try {
            service.update(entradasDto);
            return new ResponseEntity<>(new ApiResponse(entradasDto, HttpStatus.OK), HttpStatus.OK);
        } catch (RuntimeException runtimeException) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, true, runtimeException.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete (@PathVariable Long id) {
        try {
            service.delete(id);
            return new ResponseEntity<>(new ApiResponse(id, HttpStatus.OK), HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
            return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, true, exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
