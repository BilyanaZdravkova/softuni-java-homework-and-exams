package com.softuni.springautomapptingobjects.services.impls;

import com.softuni.springautomapptingobjects.domain.dtos.UserDto;
import com.softuni.springautomapptingobjects.domain.dtos.UserLoginDto;
import com.softuni.springautomapptingobjects.domain.dtos.UserPurchaseDto;
import com.softuni.springautomapptingobjects.domain.dtos.UserRegisterDto;
import com.softuni.springautomapptingobjects.domain.entities.Game;
import com.softuni.springautomapptingobjects.domain.entities.Role;
import com.softuni.springautomapptingobjects.domain.entities.User;
import com.softuni.springautomapptingobjects.repositories.GameRepository;
import com.softuni.springautomapptingobjects.repositories.UserRepository;
import com.softuni.springautomapptingobjects.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private UserDto userDto;
    private UserPurchaseDto userPurchaseDto;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GameRepository gameRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        User user = this.modelMapper.map(userRegisterDto, User.class);

        user.setRole(this.userRepository.count() == 0 ? Role.ADMIN : Role.USER);

        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {
        User user = this.userRepository.findByEmail(userLoginDto.getEmail());

        if (user == null) {
            System.out.println("Incorrect username / password");
        } else {
            this.userDto = this.modelMapper.map(user, UserDto.class);
            System.out.println("Successfully logged in " + user.getFullName());
        }
    }

    @Override
    public void logout() {

        if (this.userDto == null) {
            System.out.println("Cannot log out. No user is logged in.");
        } else {
            String name = this.userDto.getFullName();
            this.userDto = null;
            System.out.println("User " + name + " successfully logged out.");
        }
    }

    @Override
    public boolean isLoggedInUserAdmin() {
        return this.userDto.getRole().equals(Role.ADMIN);
    }

    @Override
    public void purchaseGame(String email, String gameTitle) {

        User user = this.userRepository.findByEmail(email);

        if (user == null) {
            System.out.println("User doesn't exist!");
        } else {
            this.userPurchaseDto = this.modelMapper.map(user, UserPurchaseDto.class);
            Set<Game> games = this.userPurchaseDto.getGames();
            try {
                games.add(this.gameRepository.findByTitle(gameTitle));
            } catch (Exception e) {
                System.out.println("No game with such title exists!");
            }
            this.userPurchaseDto.setGames(games);
            this.userRepository.delete(user);
            User user1 = this.modelMapper.map(userPurchaseDto, User.class);
            this.userRepository.saveAndFlush(user1);
        }
    }

    @Override
    public void viewOwnedGames() {
        if (this.userDto == null) {
            System.out.println("Cannot view owned games. No user is logged in.");
        } else {
            String email = this.userDto.getEmail();
            User user = this.userRepository.findByEmail(email);
            this.userPurchaseDto = this.modelMapper.map(user, UserPurchaseDto.class);
            this.userPurchaseDto.getGames().forEach(game -> System.out.println(game.getTitle()));
        }
    }
}
