package issste.gob.mx.SISADI.controller;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dto.PersonaDto;
import issste.gob.mx.SISADI.services.PersonaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sisadi/persona")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService service;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll () {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById (@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> save (@RequestBody PersonaDto personaDto) {
        return service.register(personaDto);
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse> update (@RequestBody PersonaDto personaDto) {
        try {
            service.update(personaDto);
            return new ResponseEntity<>(new ApiResponse(personaDto, HttpStatus.OK), HttpStatus.OK);
        } catch (RuntimeException runtimeException){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, runtimeException.getMessage()), HttpStatus.BAD_REQUEST);
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
