package issste.gob.mx.SISADI.security.entity;

import issste.gob.mx.SISADI.model.entity.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id_usuario;
    private String usuario;
    private String contrasena;

    public static UserDetailsImpl build(Usuario usuario){
        return new UserDetailsImpl(usuario.getId_usuario(), usuario.getUsuario(), usuario.getContrasena());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.emptyList();
    }
    @Override
    public String getPassword(){
        return contrasena;
    }
    @Override
    public String getUsername(){
        return usuario;
    }
    public Long getId_usuario() {
        return id_usuario;
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
}
