package hu.bme.aut.freelancer_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transfer_entity")
@Getter @Setter
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
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "carrier_id", referencedColumnName = "id")
    private User carrier;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "town_id", referencedColumnName = "id")
    private Town town;

    @JsonIgnore
    @OneToMany(mappedBy = "transfer")
    private List<Package> packages;

    public boolean fitPackage(Package pack) {
        if (date.before(pack.getDateLimit())) {
            var weightSum = packages.stream()
                    .mapToDouble(Package::getWeight)
                    .sum();
            boolean weightFit = weightSum + pack.getWeight() <= vehicle.getWeightLimit();
            var packageCCSum = packages.stream()
                    .mapToInt(p -> p.getSize().getCC())
                    .sum();
            boolean ccFit = packageCCSum + pack.getSize().getCC() <= vehicle.getCC();

            return weightFit && ccFit;
        }
        return false;
    }
}
