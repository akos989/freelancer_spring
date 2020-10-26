package hu.bme.aut.freelancer_spring.model;

import hu.bme.aut.freelancer_spring.model.enums.Size;
import hu.bme.aut.freelancer_spring.model.enums.Status;

import javax.persistence.*;

@Entity
@Table(name = "package_entity")
public class Package {

    @Id
    @GeneratedValue(generator = "package_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "package_id_seq",
            sequenceName = "package_id_seq"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private Size size;

    @Column(name = "weight")
    private double weight;

    @Column(name = "fromLat")
    private double fromLat;

    @Column(name = "fromLong")
    private double fromLong;

    @Column(name = "toLat")
    private double toLat;

    @Column(name = "toLong")
    private double toLong;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "senderId", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "transferId", referencedColumnName = "id")
    private Transfer transfer;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size.name();
    }

    public double getWeight() {
        return weight;
    }

    public double getFromLat() {
        return fromLat;
    }

    public double getFromLong() {
        return fromLong;
    }

    public double getToLat() {
        return toLat;
    }

    public double getToLong() {
        return toLong;
    }

    public String getStatus() {
        return status.name();
    }

    public User getSender() {
        return sender;
    }

    public Transfer getTransfer() {
        return transfer;
    }
}
