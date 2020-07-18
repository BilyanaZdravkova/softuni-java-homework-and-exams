package com.softuni.springautomapptingobjects.controllers;

import com.softuni.springautomapptingobjects.domain.dtos.GameAddDto;
import com.softuni.springautomapptingobjects.domain.dtos.GameDeleteDto;
import com.softuni.springautomapptingobjects.domain.dtos.UserLoginDto;
import com.softuni.springautomapptingobjects.domain.dtos.UserRegisterDto;
import com.softuni.springautomapptingobjects.domain.entities.Game;
import com.softuni.springautomapptingobjects.services.GameService;
import com.softuni.springautomapptingobjects.services.UserService;
import com.softuni.springautomapptingobjects.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AppController implements CommandLineRunner {

    private final BufferedReader bufferedReader;
    private final UserService userService;
    private final ValidationUtil validationUtil;
    private final GameService gameService;

    @Autowired
    public AppController(BufferedReader bufferedReader, UserService userService, ValidationUtil validationUtil, GameService gameService) {
        this.bufferedReader = bufferedReader;
        this.userService = userService;
        this.validationUtil = validationUtil;
        this.gameService = gameService;
    }

    @Override
    public void run(String... args) throws Exception {

        while (true) {
            System.out.println("Please enter command:");
            String[] input = this.bufferedReader.readLine().split("\\|");

            switch (input[0]) {
                case "RegisterUser":
                    if (!input[2].equals(input[3])) {
                        System.out.println("Password doesn't match!");
                        break;
                    }

                    UserRegisterDto userRegisterDto = new UserRegisterDto(input[1], input[2], input[4]);

                    if (this.validationUtil.isValid(userRegisterDto)) {
                        this.userService.registerUser(userRegisterDto);

                        System.out.println(input[4] + " was registered.");

                    } else {
                        this.validationUtil.getViolations(userRegisterDto)
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .forEach(System.out::println);
                    }

                    break;

                case "LoginUser":

                    UserLoginDto userLoginDto = new UserLoginDto(input[1], input[2]);
                    this.userService.loginUser(userLoginDto);
                    break;

                case "Logout":
                    this.userService.logout();
                    break;

                case "AddGame":
                    GameAddDto gameAddDto = new GameAddDto(
                            input[1], new BigDecimal(input[2]), Double.parseDouble(input[3]),
                            input[4], input[5], input[6], LocalDate.parse(input[7],
                            DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                    );

                    if (this.validationUtil.isValid(gameAddDto)) {
                        this.gameService.addGame(gameAddDto);
                    } else {
                        this.validationUtil.getViolations(gameAddDto).stream()
                                .map(ConstraintViolation::getMessage).forEach(System.out::println);
                    }
                    break;

                case "EditGame":
                    try {
                        Game gameToEdit = this.gameService.getGameById(Long.parseLong(input[1]));

                        String title = gameToEdit.getTitle();
                        BigDecimal price = gameToEdit.getPrice();
                        double size = gameToEdit.getSize();
                        String trailer = gameToEdit.getTrailer();
                        String thumbnail = gameToEdit.getImageThumbnail();
                        String description = gameToEdit.getDescription();
                        LocalDate releaseDate = gameToEdit.getReleaseDate();

                        for (int i = 2; i < input.length; i++) {
                            String[] s = input[i].split("=");

                            switch (s[0]) {
                                case "price":
                                    price = BigDecimal.valueOf(Double.parseDouble(s[1]));
                                    break;
                                case "size":
                                    size = Double.parseDouble(s[1]);
                                    break;
                                case "title":
                                    title = s[1];
                                    break;
                                case "trailer":
                                    trailer = s[1];
                                    break;
                                case "thumbnail":
                                    thumbnail = s[1];
                                    break;
                                case "description":
                                    description = s[1];
                                    break;
                                case "release date":
                                    releaseDate = LocalDate.parse(s[1], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                                    break;
                            }
                        }

                        GameAddDto gameAddDto1 = new GameAddDto(
                                title, price, size, trailer, thumbnail, description, releaseDate
                        );

                        if (this.validationUtil.isValid(gameAddDto1)) {
                            this.gameService.editGame(gameAddDto1, Long.parseLong(input[1]));
                        } else {
                            this.validationUtil.getViolations(gameAddDto1).stream()
                                    .map(ConstraintViolation::getMessage).forEach(System.out::println);
                        }
                    } catch (Exception e) {
                        System.out.println("Incorrect game id!");
                    }
                    break;

                case "DeleteGame":
                    GameDeleteDto gameDeleteDto = new GameDeleteDto(Integer.parseInt(input[1]));
                    this.gameService.deleteGame(gameDeleteDto);
                    break;

                case "AllGames":
                    this.gameService.getAll().forEach(game -> System.out.printf("%s %.2f%n", game.getTitle(), game.getPrice()));
                    break;

                case "DetailsGame":
                    Game game = this.gameService.getGameInfo(input[1]);
                    System.out.printf("Title: %s%nPrice: %.2f%nDescription: %s%nRelease date: %s%n", game.getTitle(), game.getPrice(),
                            game.getDescription(), game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    break;

                case "DetailGame":
                    //The input is typed incorrectly in the task, that's why I've made 2 cases for both the correct one and the other.
                    Game game1 = this.gameService.getGameInfo(input[1]);
                    System.out.printf("");
                    System.out.printf("Title: %s%nPrice: %.2f%nDescription: %s%nRelease date: %s%n", game1.getTitle(), game1.getPrice(),
                            game1.getDescription(), game1.getReleaseDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    break;

                case "PurchaseGame":
                    //You can purchase game with the following command to test the last task:
                    // PurchaseGame|ivan@ivan.com|Overwatch -> this is an example, change the input as you wish.
                    this.userService.purchaseGame(input[1], input[2]);
                    break;

                case "OwnedGames":
                    this.userService.viewOwnedGames();
                    break;

            }
        }
    }
}

