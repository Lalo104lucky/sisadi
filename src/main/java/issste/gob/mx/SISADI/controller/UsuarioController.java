package issste.gob.mx.SISADI.controller;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dto.UsuarioDto;
import issste.gob.mx.SISADI.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sisadi/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable("id") Long id){
        return service.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> changeContrasena (@PathVariable("id") Long id, @RequestBody UsuarioDto usuarioDto){
        return service.changeContrasena(id, usuarioDto, passwordEncoder);
    }
}
