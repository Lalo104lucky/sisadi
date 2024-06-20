package issste.gob.mx.SISADI.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InsumoDto {

    private Long id_insumo;
    private Long clave;
    private String descripcion;
    private String unidad;

    private Long usuario_id;
}
