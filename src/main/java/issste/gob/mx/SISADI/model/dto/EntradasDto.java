package issste.gob.mx.SISADI.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EntradasDto {

    private Long id_entradas;
    private Long cantidad;
    private Double total;

    private Long operacion_id;
    private Long insumos_id;
}