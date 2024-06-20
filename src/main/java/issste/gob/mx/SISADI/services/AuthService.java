package issste.gob.mx.SISADI.services;

import issste.gob.mx.SISADI.config.ApiResponse;
import issste.gob.mx.SISADI.model.dto.SignedDto;
import issste.gob.mx.SISADI.model.entity.Usuario;
import issste.gob.mx.SISADI.security.jwt.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {
    private final UsuarioService service;
    private final AuthenticationManager manager;
    private final JwtProvider provider;

    public AuthService(UsuarioService service, AuthenticationManager manager, JwtProvider provider) {
        this.service = service;
        this.manager = manager;
        this.provider = provider;
    }

    @Transactional
    public ResponseEntity<ApiResponse> signIn (String usuario, String contrasena) {
        try {
            Optional<Usuario> foundUser = service.findUser(usuario);
            if (foundUser.isEmpty()){
                return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_FOUND, true, "UserNotFound"), HttpStatus.BAD_REQUEST);
            }
            Usuario usuario1 = foundUser.get();

            Authentication auth = manager.authenticate(new UsernamePasswordAuthenticationToken(usuario, contrasena));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String token = provider.generateToken(auth);
            //SignedDto (token, tokenType, usuario, personas)
            SignedDto signedDto = new SignedDto(token, "Bearer", usuario1, usuario1.getPersona().getId_persona());
            return new ResponseEntity<>(new ApiResponse(signedDto, HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e){
            String message = "CredentialsMismatch";
            if (e instanceof DisabledException) {
                message = "UserDisabled";
            }
            return new ResponseEntity<>(new ApiResponse(HttpStatus.BAD_REQUEST, true, message), HttpStatus.BAD_REQUEST);
        }
    }
}
