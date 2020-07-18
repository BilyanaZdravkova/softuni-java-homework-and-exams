package com.softuni.springexercise.repositories;

import com.softuni.springexercise.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("select a from Author as a order by a.books.size desc")
    List<Author> findAllByCountOfBooks();

    Author findAuthorByLastName(String lastName);

    List<Author> findAllByFirstNameEndingWith(String ending);

    List<Author> findAllByLastNameIsStartingWith(String input);





}
