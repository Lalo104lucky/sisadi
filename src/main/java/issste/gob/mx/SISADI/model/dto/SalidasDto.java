package issste.gob.mx.SISADI.model.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SalidasDto {

    private Long id_salidas;
    private Long cantidad;
    private Double total;

    private Long operacion_id;
    private Long insumos_id;
}