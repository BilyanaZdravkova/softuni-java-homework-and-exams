package com.softuni.springautomapptingobjects.services.impls;

import com.softuni.springautomapptingobjects.domain.dtos.GameAddDto;
import com.softuni.springautomapptingobjects.domain.dtos.GameDeleteDto;
import com.softuni.springautomapptingobjects.domain.entities.Game;
import com.softuni.springautomapptingobjects.repositories.GameRepository;
import com.softuni.springautomapptingobjects.services.GameService;
import com.softuni.springautomapptingobjects.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final ModelMapper modelMapper;
    private final GameRepository gameRepository;
    private final UserService userService;

    @Autowired
    public GameServiceImpl(ModelMapper modelMapper, GameRepository gameRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {
        try {
            if (!this.userService.isLoggedInUserAdmin()) {
                System.out.println("Logged in user is not ADMIN!");
                return;
            }
        } catch (Exception e) {
            System.out.println("No user is logged in!");
        }

        Game game = this.modelMapper.map(gameAddDto, Game.class);

        this.gameRepository.saveAndFlush(game);
    }

    @Override
    public void editGame(GameAddDto gameAddDto, Long id) {
        try {
            if (!this.userService.isLoggedInUserAdmin()) {
                System.out.println("Logged in user is not ADMIN!");
                return;
            }
        } catch (Exception e) {
            System.out.println("No user is logged in!");
        }

        Game game = this.modelMapper.map(gameAddDto, Game.class);

        Game game1 = this.gameRepository.getOne(id);
        this.gameRepository.delete(game1);
        this.gameRepository.saveAndFlush(game);
    }

    @Override
    public void deleteGame(GameDeleteDto gameDeleteDto) {
        Game game = this.modelMapper.map(gameDeleteDto, Game.class);
        if (gameRepository.existsById(game.getId())) {
            System.out.println("Deleted " + gameRepository.findById(game.getId()).getTitle());
            this.gameRepository.delete(game);
        } else {
            System.out.println("Game ID doesn't exist!");
        }
    }

    @Override
    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    @Override
    public Game getGameInfo(String title) {
        return this.gameRepository.findByTitle(title);
    }

    @Override
    public Game getGameById(Long id) {
        return this.gameRepository.findById(id).orElse(null);
    }
}
