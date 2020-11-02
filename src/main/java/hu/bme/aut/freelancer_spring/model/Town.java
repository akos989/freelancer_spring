package hu.bme.aut.freelancer_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @JsonIgnore
    @OneToMany(mappedBy = "town")
    private List<Transfer> transfers;

    @JsonIgnore
    @OneToMany(mappedBy = "town")
    private List<Package> packages;
}
