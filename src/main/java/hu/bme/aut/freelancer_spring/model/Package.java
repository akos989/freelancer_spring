package hu.bme.aut.freelancer_spring.model;

import hu.bme.aut.freelancer_spring.model.enums.Size;
import hu.bme.aut.freelancer_spring.model.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "package_entity")
@Getter @Setter
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

    @Column(name = "from_lat")
    private double fromLat;

    @Column(name = "from_long")
    private double fromLong;

    @Column(name = "to_lat")
    private double toLat;

    @Column(name = "to_long")
    private double toLong;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "transfer_id", referencedColumnName = "id")
    private Transfer transfer;

    public Package() {
        status = Status.WAITING;
    }

    public void setStatus(Status status) {
        this.status = Objects.requireNonNullElse(status, Status.WAITING);
    }
}
