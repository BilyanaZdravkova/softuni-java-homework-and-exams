package com.softuni.springexercise.services.impl;

import com.softuni.springexercise.entities.*;
import com.softuni.springexercise.repositories.BookRepository;
import com.softuni.springexercise.services.AuthorService;
import com.softuni.springexercise.services.BookService;
import com.softuni.springexercise.services.CategoryService;
import com.softuni.springexercise.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.softuni.springexercise.constants.GlobalConstants.BOOKS_FILE_PATH;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final FileUtil fileUtil;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService, FileUtil fileUtil) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.fileUtil = fileUtil;
    }

    @Override
    public void seedBooks() throws IOException {
        if (this.bookRepository.count() != 0) {
            return;
        }

        String[] fileContent = this.fileUtil
                .readFileContent(BOOKS_FILE_PATH);

        Arrays.stream(fileContent).forEach(r -> {
            String[] params = r.split("\\s+");

            Author author = this.getRandomAuthor();

            EditionType editionType = EditionType.values()[Integer.parseInt(params[0])];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate releaseDate = LocalDate.parse(params[1], formatter);

            int copies = Integer.parseInt(params[2]);

            BigDecimal price = new BigDecimal(params[3]);

            AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(params[4])];

            String title = this.getTitle(params);

            Set<Category> categorySet = this.getRandomCategories();
            Book book = new Book();
            book.setAuthor(author);
            book.setEditionType(editionType);
            book.setReleaseDate(releaseDate);
            book.setCopies(copies);
            book.setPrice(price);
            book.setAgeRestriction(ageRestriction);
            book.setTitle(title);
            book.setCategories(categorySet);

            this.bookRepository.saveAndFlush(book);
        });

    }

    @Override
    public List<Book> getAllBooksAfter2k() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate releaseDate = LocalDate.parse("31/12/2000", formatter);


        return this.bookRepository.findAllByReleaseDateAfter(releaseDate);
    }

    @Override
    public List<Book> getAllBooksBefore(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        LocalDate releaseDate = LocalDate.parse(date, formatter);

        return this.bookRepository.findAllByReleaseDateBefore(releaseDate);
    }

    @Override
    public List<Book> findAllByAuthor() {
        Author author = authorService.findAuthorByLastName("Powell");
        return this.bookRepository.findAllByAuthorOrderByReleaseDateDescTitleAsc(author);
    }

    @Override
    public List<Book> getAllBooksByAgeRestriction(String ageRestriction) {
        return this.bookRepository.findAllByAgeRestriction(AgeRestriction.valueOf(ageRestriction.toUpperCase()));
    }

    @Override
    public List<Book> getAllBooksByEditionTypeAndCopies(String editionType, int copies) {
        return this.bookRepository.findAllByEditionTypeAndCopiesLessThan(EditionType.valueOf(editionType.toUpperCase()), copies);
    }

    @Override
    public List<Book> getTitlesAndPriceOfBooksBetweenTwoPrices(BigDecimal firstPrice, BigDecimal secondPrice) {
        return this.bookRepository.findAllByPriceLessThanOrPriceGreaterThan(firstPrice, secondPrice);
    }

    @Override
    public List<Book> getAllBooksWithReleaseNotTheFollowing(int date) {
        LocalDate dt = LocalDate.parse(date + "-01-01");
        LocalDate dt2 = LocalDate.parse((date + 1) + "-01-01");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        formatter.format(dt);
        formatter.format(dt2);

        return this.bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(dt, dt2);
    }

    @Override
    public List<Book> getAllByTitleContaining(String input) {
        return this.bookRepository.findAllByTitleContaining(input);
    }

    @Override
    public List<Book> getAllByTitle(int length) {
        List<Book> books = this.bookRepository.findAll();
        books.removeIf(book -> book.getTitle().length() <= length);

        return books;
    }

    @Override
    public Book findByTitle(String title) {
        return this.bookRepository.findByTitle(title);
    }

    private Set<Category> getRandomCategories() {
        Random random = new Random();
        int bound = random.nextInt(3) + 1;

        Set<Category> result = new HashSet<>();
        for (int i = 1; i <= bound; i++) {
            int categoryId = random.nextInt(8) + 1;
            result.add(this.categoryService.getCategoryById((long) categoryId));
        }

        return result;
    }

    private String getTitle(String[] params) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 5; i < params.length; i++) {
            stringBuilder.append(params[i]).append(" ");
        }

        return stringBuilder.toString().trim();
    }

    private Author getRandomAuthor() {
        Random random = new Random();

        int randomNum =
                random.nextInt(this.authorService.getAllAuthorsCount()) + 1;


        return this.authorService.findAuthorById((long) randomNum);
    }
}
