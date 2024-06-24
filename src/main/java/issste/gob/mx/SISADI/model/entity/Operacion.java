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
    @Column(name = "folio", nullable = false)
    private String folio;
    @Column(name = "nom_operacion", nullable = false)
    private String nom_operacion;
    @Column(name = "fecha_inicial")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp fecha_inicial;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp fecha_final;
    @Column(name = "unidad", nullable = false)
    private String unidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "existencias_id")
    private Existencias existencias;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operacion", orphanRemoval = true)
    @JsonIgnore
    private Set<Entradas> entradas;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "operacion", orphanRemoval = true)
    @JsonIgnore
    private Set<Salidas> salidas;
}
