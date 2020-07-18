package softuni.exam.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {
    private String name;
    private LocalDateTime dateAndTime;
    private Car car;
    private Set<Offer> offers;

    @ManyToMany (mappedBy = "pictures")
    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

    @ManyToOne (cascade = CascadeType.MERGE)
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Picture() {
    }

    @Column (name = "name", unique = true)
    @Length(min = 3, max = 19)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
