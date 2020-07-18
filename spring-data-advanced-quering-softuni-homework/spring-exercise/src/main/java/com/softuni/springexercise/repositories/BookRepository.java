package com.softuni.springexercise.repositories;

import com.softuni.springexercise.entities.AgeRestriction;
import com.softuni.springexercise.entities.Author;
import com.softuni.springexercise.entities.Book;
import com.softuni.springexercise.entities.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate localDate);

    List<Book> findAllByReleaseDateBefore(LocalDate localDate);

    List<Book> findAllByAuthorOrderByReleaseDateDescTitleAsc(Author author);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, int copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan (BigDecimal lower, BigDecimal higher);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate firstDate, LocalDate secondDate);

    List<Book> findAllByTitleContaining(String input);

    Book findByTitle(String title);


}
