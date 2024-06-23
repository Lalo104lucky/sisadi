package issste.gob.mx.SISADI.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;
    @Column(name = "usuario", nullable = false)
    private String usuario;
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Persona persona;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario")
    @JsonIgnore
    private Set<Proveedor> proveedors;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario")
    @JsonIgnore
    private Set<Insumo> insumos;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario")
    @JsonIgnore
    private Set<Operacion> operacions;
}
