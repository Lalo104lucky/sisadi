package issste.gob.mx.SISADI.model.dto;

import issste.gob.mx.SISADI.model.entity.Usuario;
import lombok.Value;

@Value
public class SignedDto {
    String token;
    String tokenType;
    Usuario usuario;
    Long personas;
}
