package hu.bme.aut.freelancer_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @Column(name = "from_lat")
    private double fromLat;

    @Column(name = "from_long")
    private double fromLong;

    @Column(name = "to_lat")
    private double toLat;

    @Column(name = "to_long")
    private double toLong;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "encoded_route")
    private String encodedRoute;

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
    private List<Package> packages = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        for (var pack : packages) {
            pack.setTransfer(null);
        }
    }

    public boolean fitPackage(Package newPackage) {
        if (date.before(newPackage.getDateLimit()) && suitableInsurance(newPackage)) {
            return vehicle.isBelowWeightLimit(getPackagesWeightSum() + newPackage.getWeight())
                    && vehicle.isBelowCCLimit(getPackagesCCSum() + newPackage.getSize().getCC());
        }
        return false;
    }

    private boolean suitableInsurance(Package newPackage) {
        return !newPackage.needInsurance() || carrier.isHasInsurance();
    }

    private double getPackagesWeightSum() {
        return packages.stream()
                .mapToDouble(Package::getWeight)
                .sum();
    }

    private int getPackagesCCSum() {
        return packages.stream()
                .mapToInt(p -> p.getSize().getCC())
                .sum();
    }

    public void addPackage(Package pack) {
        packages.add(pack);
    }
}
