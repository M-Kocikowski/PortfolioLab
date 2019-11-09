package pl.coderslab.charity.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    private int quantity;

    @Setter
    @ManyToMany
    private List<Category> categories;

    @Setter
    @ManyToOne
    private Institution institution;

    @Setter
    private String street;

    @Setter
    private String city;

    @Setter
    private String zipCode;

    @Setter
    private LocalDate pickUpDate;

    @Setter
    private LocalTime pickUpTime;

    @Setter
    private String pickUpComment;

}
