package issste.gob.mx.SISADI.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "salidas")
public class Salidas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salidas")
    private Long id_salidas;
    @Column(name = "cantidad", nullable = false)
    private Long cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipoInsumo_id")
    private TipoInsumo tipoInsumo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operacion_id")
    private Operacion operacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salidas", orphanRemoval = true)
    @JsonIgnore
    private Set<Control> controls;

}
