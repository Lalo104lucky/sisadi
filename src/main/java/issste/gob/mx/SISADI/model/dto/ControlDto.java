package issste.gob.mx.SISADI.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ControlDto {

    private Long id_control;
    private Double imp_inv_inicial;
    private Double imp_entradas;
    private Double imp_salidas;
    private Double precio_final;
    private Double imp_inv_final;

    private Long entradas_id;
    private Long salidas_id;

}
