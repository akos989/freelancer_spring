package hu.bme.aut.freelancer_spring.model;

import javax.persistence.*;

@Entity
@Table(name = "vehicle_entity")
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getWeightLimit() {
        return weightLimit;
    }

    public User getOwner() {
        return owner;
    }
}
