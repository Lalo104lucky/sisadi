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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipoInsumo_id")
    private TipoInsumo tipoInsumo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operacion_id")
    private Operacion operacion;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "entradas", orphanRemoval = true)
    @JsonIgnore
    private Set<Control> controls;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

}
