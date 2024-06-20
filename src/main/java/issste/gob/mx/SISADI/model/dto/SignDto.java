package issste.gob.mx.SISADI.model.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignDto {
    @NotBlank
    @NotEmpty
    private String usuario;
    @NotBlank
    @NotEmpty
    private String contrasena;

}
