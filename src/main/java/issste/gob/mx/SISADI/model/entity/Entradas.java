package issste.gob.mx.SISADI.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "entradas")
public class Entradas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entradas")
    private Long id_entradas;
    @Column(name = "cantidad", nullable = false)
    private Long cantidad;
    @Column(name = "total", nullable = false)
    private Double total;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "entradas")
    @JsonIgnore
    private Existencias existencias;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operacion_id")
    private Operacion operacion;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "insumos_id")
    private Insumo insumo;
}