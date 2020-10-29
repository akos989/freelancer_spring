package hu.bme.aut.freelancer_spring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "town_entity")
@Getter @Setter
public class Town {

    @Id
    @GeneratedValue(generator = "town_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "town_id_seq",
            sequenceName = "town_id_seq"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;
}
