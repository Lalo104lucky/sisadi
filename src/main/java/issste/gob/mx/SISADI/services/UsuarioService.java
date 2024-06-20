package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dao.UsuarioRepository;
import issste.gob.mx.SISADI.model.dto.UsuarioDto;
import issste.gob.mx.SISADI.model.entity.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository repository;


    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<Usuario> findUser(String username){
        return repository.findByUsuario(username);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findAll(){
         return new ResponseEntity<>(new ApiResponse(repository.findAll(), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id){
        Optional<Usuario> foundUsuario = repository.findById(id);
        if (foundUsuario.isPresent()){
            return new ResponseEntity<>(new ApiResponse(foundUsuario.get(), HttpStatus.OK), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "UserNotFound"), HttpStatus.BAD_REQUEST);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<ApiResponse> changeContrasena(Long id, UsuarioDto usuarioDto, PasswordEncoder passwordEncoder){
        Optional<Usuario> foundUsuario = repository.findById(id);
        if (foundUsuario.isEmpty()){
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, "UserNotFound"), HttpStatus.BAD_REQUEST);
        }
        Usuario usuario = foundUsuario.get();
        String encryptedPassword = passwordEncoder.encode(usuarioDto.getContrasena());
        usuario.setContrasena(encryptedPassword);

        return new ResponseEntity<>(new ApiResponse(repository.save(usuario), HttpStatus.OK), HttpStatus.OK);
    }

}
