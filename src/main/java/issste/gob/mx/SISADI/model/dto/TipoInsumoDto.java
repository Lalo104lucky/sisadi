package issste.gob.mx.SISADI.model.dto;

import lombok.*;

import java.util.Set;

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
    private String unidad;

    private Set<Long> insumo_id;
}
