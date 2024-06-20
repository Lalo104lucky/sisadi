package issste.gob.mx.SISADI.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PersonaDto {

    private Long id_persona;
    private String nombre;
    private String apellido_p;
    private String apellido_m;

    private Long usuario_id;
}
