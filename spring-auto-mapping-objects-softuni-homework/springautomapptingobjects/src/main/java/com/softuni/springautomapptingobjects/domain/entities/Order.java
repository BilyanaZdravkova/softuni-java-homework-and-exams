package com.softuni.springautomapptingobjects.domain.entities;

import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private User user;
    private Set<Game> orderedGames;

    public Order() {
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Set<Game> getOrderedGames() {
        return orderedGames;
    }

    public void setOrderedGames(Set<Game> orderedGames) {
        this.orderedGames = orderedGames;
    }

    @JoinColumn(name = "buyer_id")
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
