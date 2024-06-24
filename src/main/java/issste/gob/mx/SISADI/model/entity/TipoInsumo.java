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
@Table(name = "tipoinsumo")
public class TipoInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipoinsumo")
    private Long id_tipoinsumo;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "partida", nullable = true)
    private Long partida;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "tipoInsumos")
    private Set<Insumo> insumos;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tipoInsumo", orphanRemoval = true)
    @JsonIgnore
    private Set<Existencias> existencias;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tipoInsumo", orphanRemoval = true)
    @JsonIgnore
    private Set<Entradas> entradas;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tipoInsumo", orphanRemoval = true)
    @JsonIgnore
    private Set<Salidas> salidas;
}
