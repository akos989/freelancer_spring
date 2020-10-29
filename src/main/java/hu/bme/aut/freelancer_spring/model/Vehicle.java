package hu.bme.aut.freelancer_spring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    @Column(name = "weightLimit")
    private String weightLimit;

    @ManyToOne
    @JoinColumn(name = "ownerId", referencedColumnName = "id")
    private User owner;
}