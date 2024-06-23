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
@Table(name = "existencias")
public class Existencias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_existencias")
    private Long id_existencias;
    @Column(name = "existencia", nullable = false)
    private Long existencia;
    @Column(name = "importe", nullable = false)
    private Double importe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipoinsumo_id")
    private TipoInsumo tipoInsumo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "existencias", orphanRemoval = true)
    @JsonIgnore
    private Set<Operacion> operacions;
}
