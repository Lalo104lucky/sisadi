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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "entradas", orphanRemoval = true)
    @JsonIgnore
    private Set<Existencias> existencias;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operacion_id", nullable = false)
    private Operacion operacion;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "entradas")
    private Set<Insumo> insumos;
}