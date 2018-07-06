package com.flockse.platformbackend.controllers;

import com.datastax.driver.core.utils.UUIDs;
import com.flockse.platformbackend.domain.Book;
import com.flockse.platformbackend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class RootController {

    private final BookRepository bookRepository;

    @Autowired
    public RootController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public ResponseEntity<String> getRoot() {

        Book javaBook = new Book(UUIDs.timeBased(), "Head First Java", "O'Reilly Media", new HashSet<>(Arrays.asList("Computer", "Software")));
        bookRepository.save(javaBook);

        List<String> collect = bookRepository.findAll().stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());

        return new ResponseEntity<>(collect.stream().collect(Collectors.joining()), HttpStatus.OK);
    }

}
