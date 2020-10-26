package hu.bme.aut.freelancer_spring.model;

import javax.persistence.*;

@Entity
@Table(name = "user_entity")
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

    public User(String name, String email, String phonenumber, String password) {
        this.name = name;
        this.email = email;
        this.phonenumber = phonenumber;
        this.password = password;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getPassword() {
        return password;
    }
}
