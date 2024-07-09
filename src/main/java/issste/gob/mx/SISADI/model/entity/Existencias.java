package issste.gob.mx.SISADI.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "cantidad", nullable = false)
    private Long cantidad;
    @Column(name = "total", nullable = false)
    private Double total;

    //tienen que ir al revez, es OneToMany, para que así de varias salidas, ente sola una existencia
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "salidas_id")
    private Salidas salidas;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "entradas_id")
    private Entradas entradas;

}
