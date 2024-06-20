package issste.gob.mx.SISADI.model.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OperacionDto {

    private Long id_operacion;
    private String folio;
    private String nom_operacion;
    private Timestamp fecha_inicial;
    private Timestamp fecha_final;

    private Long usuario_id;
    private Long existencias_id;
}
