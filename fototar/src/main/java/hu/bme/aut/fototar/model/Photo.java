package hu.bme.aut.fototar.model;

import javax.persistence.*;

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(generator = "photo_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "photo_id_seq",
            sequenceName = "photo_id_seq"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "owner")
    private String owner;

    @Column(name = "price")
    private int price;

    public Photo() {
    }

    public Photo(Long id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public int getPrice() {
        return price;
    }
}
