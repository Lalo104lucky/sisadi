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

    private Long tipoInsumo_id;
    private Long operacion_id;
    private Long proveedor_id;
}
