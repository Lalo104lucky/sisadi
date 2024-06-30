package issste.gob.mx.SISADI.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "operacion")
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacion")
    private Long id_operacion;
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "folio", nullable = false)
    private String folio;
    @Column(name = "tipo_movimiento", nullable = false)
    private String tipo_movimiento;
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp fecha;
    @Column(name = "unidad", nullable = false)
    private String unidad;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Control> controls;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Entradas> entradas;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Salidas> salidas;
}
