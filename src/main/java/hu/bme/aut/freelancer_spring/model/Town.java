package hu.bme.aut.freelancer_spring.model;

import javax.persistence.*;

@Entity
@Table(name = "town_entity")
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
