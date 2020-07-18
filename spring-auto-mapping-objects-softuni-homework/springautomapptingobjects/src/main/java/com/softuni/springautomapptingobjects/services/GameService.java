package com.softuni.springautomapptingobjects.services;

import com.softuni.springautomapptingobjects.domain.dtos.GameAddDto;
import com.softuni.springautomapptingobjects.domain.dtos.GameDeleteDto;
import com.softuni.springautomapptingobjects.domain.entities.Game;

import java.util.List;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    void editGame(GameAddDto gameAddDto, Long id);

    void deleteGame(GameDeleteDto gameDeleteDto);

    List<Game> getAll();

    Game getGameInfo(String title);

    Game getGameById(Long id);
}
