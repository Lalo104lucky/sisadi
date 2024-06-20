package issste.gob.mx.SISADI.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "control")
public class Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_control")
    private Long id_control;
    @Column(name = "imp_inv_inicial", nullable = false)
    private Double imp_inv_inicial;
    @Column(name = "imp_entradas")
    private Double imp_entradas;
    @Column(name = "imp_salidas")
    private Double imp_salidas;
    @Column(name = "precio_final", nullable = false)
    private Double precio_final;
    @Column(name = "imp_inv_final", nullable = false)
    private Double imp_inv_final;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrada_id")
    private Entradas entradas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salida_id")
    private Salidas salidas;
}
