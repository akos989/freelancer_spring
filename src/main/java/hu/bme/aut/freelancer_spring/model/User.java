package hu.bme.aut.freelancer_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_entity")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "user_id_seq",
            sequenceName = "user_id_seq"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    private List<Package> packages;

    @JsonIgnore
    @OneToMany(mappedBy = "carrier")
    private List<Transfer> transfers;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<Vehicle> vehicles;
}
