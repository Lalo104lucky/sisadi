package issste.gob.mx.SISADI.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ExistenciasDto {

    private Long id_existencias;
    private Long cantidad;
    private Double total;

    private Long salidas_id;
    private Long entradas_id;
}
