package com.softuni.springautomapptingobjects.domain.dtos;

import com.softuni.springautomapptingobjects.domain.entities.Game;
import com.softuni.springautomapptingobjects.domain.entities.Role;

import java.util.Set;

public class UserPurchaseDto {
    private String email;
    private String password;
    private String fullName;
    private Set<Game> games;
    private Role role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserPurchaseDto() {
    }

    public UserPurchaseDto(String email, String password, String fullName, Set<Game> games, Role role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.games = games;
        this.role = role;
    }
}
