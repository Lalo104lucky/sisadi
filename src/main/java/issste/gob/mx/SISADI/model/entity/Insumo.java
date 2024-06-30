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
@Table(name = "insumo")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Long id_insumo;
    @Column(name = "clave", nullable = false)
    private Long clave;
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    @Column(name = "precio", nullable = false)
    private Double precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "insumo_has_tipoinsumos", joinColumns = @JoinColumn(name = "insumo_id"),
            inverseJoinColumns = @JoinColumn(name = "tipoinsumos_id"))
    @JsonIgnore
    private Set<TipoInsumo> tipoInsumos;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "insumo")
    @JsonIgnore
    private Entradas entradas;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "insumo")
    @JsonIgnore
    private Salidas salidas;
}
