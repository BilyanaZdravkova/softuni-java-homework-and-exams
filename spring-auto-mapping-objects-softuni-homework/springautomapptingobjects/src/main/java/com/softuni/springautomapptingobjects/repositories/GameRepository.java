package com.softuni.springautomapptingobjects.repositories;

import com.softuni.springautomapptingobjects.domain.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository <Game, Long> {
    Game findByTitle(String title);

    Game findById(long id);
}
