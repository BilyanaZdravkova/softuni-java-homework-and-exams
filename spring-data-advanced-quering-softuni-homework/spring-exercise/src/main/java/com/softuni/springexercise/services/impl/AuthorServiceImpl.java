package com.softuni.springexercise.services.impl;

import com.softuni.springexercise.entities.Author;
import com.softuni.springexercise.entities.Book;
import com.softuni.springexercise.repositories.AuthorRepository;
import com.softuni.springexercise.services.AuthorService;
import com.softuni.springexercise.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import static com.softuni.springexercise.constants.GlobalConstants.AUTHOR_FILE_PATH;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() != 0) {
            return;
        }

        String[] fileContent = this.fileUtil
                .readFileContent(AUTHOR_FILE_PATH);

        Arrays.stream(fileContent).forEach(r -> {
            String[] params = r.split("\\s+");

            Author author = new Author(params[0], params[1]);

            this.authorRepository.saveAndFlush(author);
        });
    }

    @Override
    public int getAllAuthorsCount() {
        return (int) this.authorRepository.count();
    }

    @Override
    public Author findAuthorById(Long id) {
        return this.authorRepository.getOne(id);
    }

    @Override
    public Author findAuthorByLastName(String lastName) {
        return this.authorRepository.findAuthorByLastName(lastName);
    }

    @Override
    public List<Author> findAllAuthorsByCountOfBooks() {
        return this.authorRepository.findAllByCountOfBooks();
    }

    @Override
    public TreeMap<Integer, String> getAllAuthors() {
        TreeMap<Integer, String> authorsCopies = new TreeMap<>(Collections.reverseOrder());
        List<Author> authors = this.authorRepository.findAll();
        for (Author author : authors) {
            int sum = author.getBooks().stream().mapToInt(Book::getCopies).sum();
            authorsCopies.put(sum, author.getFirstName() + " " + author.getLastName());
        }

        return authorsCopies;
    }

    @Override
    public List<Author> getAllAuthorsByLastNameEndingWith(String ending) {
        return this.authorRepository.findAllByFirstNameEndingWith(ending);
    }

    @Override
    public List<Author> getAllWithLastNameStartingWithGivenString(String input) {
        return this.authorRepository.findAllByLastNameIsStartingWith(input);
    }


}
