package com.softuni.springexercise.services;

import com.softuni.springexercise.entities.Book;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> getAllBooksAfter2k();

    List<Book> getAllBooksBefore(String date);

    List<Book> findAllByAuthor();

    List<Book> getAllBooksByAgeRestriction(String ageRestriction);

    List<Book> getAllBooksByEditionTypeAndCopies(String editionType, int copies);

    List<Book> getTitlesAndPriceOfBooksBetweenTwoPrices(BigDecimal firstPrice, BigDecimal secondPrice);

    List<Book> getAllBooksWithReleaseNotTheFollowing(int date);

    List<Book> getAllByTitleContaining(String input);

    List<Book> getAllByTitle (int length);

    Book findByTitle (String title);
}
