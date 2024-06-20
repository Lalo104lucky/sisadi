package issste.gob.mx.SISADI.security.service;

import issste.gob.mx.SISADI.model.entity.Usuario;
import issste.gob.mx.SISADI.security.entity.UserDetailsImpl;
import issste.gob.mx.SISADI.services.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioService service;

    public UserDetailsServiceImpl(UsuarioService service) {
        this.service = service;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> foundUsuario = service.findUser(username);
        if(foundUsuario.isPresent()){
            return UserDetailsImpl.build(foundUsuario.get());
        }
        throw new UsernameNotFoundException("UserNotFound");
    }
}
