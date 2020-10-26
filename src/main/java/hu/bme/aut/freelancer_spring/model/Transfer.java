package hu.bme.aut.freelancer_spring.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transfer_entity")
public class Transfer {

    @Id
    @GeneratedValue(generator = "transfer_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "transfer_id_seq",
            sequenceName = "transfer_id_seq"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "carrierId", referencedColumnName = "id")
    private User carrier;

    @ManyToOne
    @JoinColumn(name = "vehicleId", referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "townId", referencedColumnName = "id")
    private Town town;

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public User getCarrier() {
        return carrier;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Town getTown() {
        return town;
    }
}
