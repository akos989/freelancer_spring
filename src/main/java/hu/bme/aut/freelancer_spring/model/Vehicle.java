package hu.bme.aut.freelancer_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "vehicle_entity")
@Getter @Setter
public class Vehicle {

    @Id
    @GeneratedValue(generator = "vehicle_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "vehicle_id_seq",
            sequenceName = "vehicle_id_seq"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;

    @Column(name = "z")
    private int z;

    @Column(name = "weight_limit")
    private double weightLimit;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle")
    private List<Transfer> transfers;

    public int getCC() {
        return x * y * z;
    }
}
