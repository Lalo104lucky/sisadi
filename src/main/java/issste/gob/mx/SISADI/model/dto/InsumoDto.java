package issste.gob.mx.SISADI.model.dto;

import jakarta.validation.constraints.Digits;
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
    @Digits(integer = 10, fraction = 2)
    private Double precio;

    private Long usuario_id;
}
