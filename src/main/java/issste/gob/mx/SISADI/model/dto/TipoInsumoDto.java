package issste.gob.mx.SISADI.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TipoInsumoDto {

    private Long id_tipoinsumo;
    private String nombre;
    private Long partida;

    private Long insumo_id;
}
