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
    private Long existencia;
    private Double precio;
    private Double importe;

    private Long tipoInsumo_id;
}
