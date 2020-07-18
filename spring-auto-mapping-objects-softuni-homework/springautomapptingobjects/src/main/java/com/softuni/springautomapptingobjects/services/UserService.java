package com.softuni.springautomapptingobjects.services;

import com.softuni.springautomapptingobjects.domain.dtos.UserLoginDto;
import com.softuni.springautomapptingobjects.domain.dtos.UserRegisterDto;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logout();

    boolean isLoggedInUserAdmin();

    void purchaseGame(String email, String gameTitle);

    void viewOwnedGames();
}
