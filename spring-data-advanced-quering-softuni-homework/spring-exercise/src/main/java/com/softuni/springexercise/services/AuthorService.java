package com.softuni.springexercise.services;

import com.softuni.springexercise.entities.Author;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface AuthorService  {
    void seedAuthors() throws IOException;

    int getAllAuthorsCount();

    Author findAuthorById (Long id);

    Author findAuthorByLastName (String lastName);

    List<Author> findAllAuthorsByCountOfBooks();

    TreeMap<Integer, String> getAllAuthors();

    List<Author> getAllAuthorsByLastNameEndingWith(String ending);

    List<Author> getAllWithLastNameStartingWithGivenString (String input);


}
