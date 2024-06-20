package issste.gob.mx.SISADI.model.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProveedorDto {

    private Long id_proveedor;
    private String nombre;
    private String rfc;

    private Long usuario_id;
}
