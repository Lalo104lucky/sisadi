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
    private String nombre;
    private String folio;
    private String tipo_movimiento;
    private Timestamp fecha;
    private String unidad;

    private Long proveedor_id;
}
