package hu.bme.aut.freelancer_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.bme.aut.freelancer_spring.model.enums.Size;
import hu.bme.aut.freelancer_spring.model.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "package_entity")
@Getter @Setter
public class Package {

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private static final int NO_INSURANCE_LIMIT = 30000;

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

    @Column(name = "value")
    private int value;

    @Column(name = "date_limit")
    @Temporal(TemporalType.DATE)
    private Date dateLimit;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    private Town town;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "transfer_id", referencedColumnName = "id")
    private Transfer transfer;

    public Package() {
        status = Status.WAITING;
        createdAt = new Date();
    }

    public void setStatus(Status status) {
        this.status = Objects.requireNonNullElse(status, Status.WAITING);
    }

    public boolean needInsurance() {
        return value > NO_INSURANCE_LIMIT;
    }
}
