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

    @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
    @JoinColumn(name = "salidas_id")
    private Salidas salidas;

    @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true)
    @JoinColumn(name = "entradas_id")
    private Entradas entradas;

}
