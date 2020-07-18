package com.softuni.springexercise.controllers;

import com.softuni.springexercise.entities.Author;
import com.softuni.springexercise.entities.Book;
import com.softuni.springexercise.services.AuthorService;
import com.softuni.springexercise.services.BookService;
import com.softuni.springexercise.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class AppController implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public AppController(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        this.categoryService.seedCategories();
        this.authorService.seedAuthors();
        this.bookService.seedBooks();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a number from 1 to 11 to select a task or enter END to exit the application.");
        String input = scanner.nextLine();

        while (!input.equals("END")) {

            switch (input) {

                case "1":
                    //Task 1: Books Titles by Age Restriction
                    System.out.println("Task one:");
                    System.out.println("Please enter age restriction - minor/teen/adult.");
                    this.bookService.getAllBooksByAgeRestriction(this.bufferedReader.readLine())
                            .stream()
                            .map(Book::getTitle)
                            .forEach(System.out::println);
                    break;

                case "2":
                    //Task 2: Golden Books
                    System.out.println("Task two:");
                    this.bookService.getAllBooksByEditionTypeAndCopies("GOLD", 5000)
                            .stream()
                            .map(Book::getTitle)
                            .forEach(System.out::println);
                    break;

                case "3":
                    //Task 3:
                    System.out.println("Task three");
                    this.bookService.getTitlesAndPriceOfBooksBetweenTwoPrices(BigDecimal.valueOf(5), BigDecimal.valueOf(40))
                            .forEach(b -> System.out.printf("%s - $%.2f%n", b.getTitle(), b.getPrice()));
                    break;

                case "4":
                    //Task 4:
                    System.out.println("Task four:");
                    System.out.println("Please enter release year.");
                    this.bookService.getAllBooksWithReleaseNotTheFollowing(Integer.parseInt(this.bufferedReader.readLine()))
                            .stream()
                            .map(Book::getTitle)
                            .forEach(System.out::println);
                    break;

                case "5":
                    //Task 5:
                    System.out.println("Task five:");
                    System.out.println("Please enter release date.");
                    this.bookService.getAllBooksBefore(this.bufferedReader.readLine())
                            .forEach(b -> System.out.printf("%s %s %.2f%n", b.getTitle(), b.getEditionType(), b.getPrice()));
                    break;

                case "6":
                    //Task 6:
                    System.out.println("Task six:");
                    System.out.println("Please enter a string.");
                    this.authorService.getAllAuthorsByLastNameEndingWith(this.bufferedReader.readLine())
                            .forEach(a -> System.out.printf("%s %s%n", a.getFirstName(), a.getLastName()));
                    break;

                case "7":
                    //Task 7:
                    System.out.println("Task seven:");
                    System.out.println("Please enter a string.");
                    this.bookService.getAllByTitleContaining(this.bufferedReader.readLine())
                            .forEach(b -> System.out.println(b.getTitle()));
                    break;

                case "8":
                    //Task 8:
                    System.out.println("Task eight:");
                    System.out.println("Please enter a string.");
                    List<Author> authors = this.authorService.getAllWithLastNameStartingWithGivenString(this.bufferedReader.readLine());
                    for (Author author : authors) {
                        Set<Book> books = author.getBooks();
                        for (Book book : books) {
                            System.out.printf("%s (%s %s)%n", book.getTitle(), author.getFirstName(), author.getLastName());
                        }
                    }
                    break;

                case "9":
                    //Task 9:
                    System.out.println("Task nine:");
                    System.out.println("Please enter a number.");
                    System.out.println(this.bookService.getAllByTitle(Integer.parseInt(this.bufferedReader.readLine())).toArray().length);
                    break;

                case "10":
                    //Task 10:
                    System.out.println("Task ten:");
                    TreeMap<Integer, String> authorsCopies = this.authorService.getAllAuthors();
                    for (Map.Entry<Integer, String> integerStringEntry : authorsCopies.entrySet()) {
                        System.out.println(integerStringEntry.getValue() + " - " + integerStringEntry.getKey());
                    }
                    break;

                case "11":
                    //Task 11:
                    System.out.println("Task elever:");
                    System.out.println("Please enter a book title.");
                    Book book = this.bookService.findByTitle(this.bufferedReader.readLine());
                    System.out.printf("%s %s %s %.2f", book.getTitle(), book.getEditionType(), book.getAgeRestriction(), book.getPrice());
                    break;

                default:
                    System.out.println("Incorrect input. Please enter a number from 1 to 11 (or END if you want to exit).");
                    break;
            }

            System.out.println();
            System.out.println("Enter a number from 1 to 11 to select a task or enter END to exit the application.");
            input = scanner.nextLine();
        }
    }
}
