package com.flockse.platformbackend.controllers;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

//    private final BookRepository bookRepository;

//    @Autowired
//    public RootController(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }

    @GetMapping
    public ResponseEntity<String> getRoot() {

//        Book javaBook = new Book(UUIDs.timeBased(), "Head First Java", "O'Reilly Media", new HashSet<>(Arrays.asList("Computer", "Software")));
//        bookRepository.save(javaBook);
//
//        List<String> collect = bookRepository.findAll().stream()
//                .map(Book::getTitle)
//                .collect(Collectors.toList());

        return new ResponseEntity<>(query(), HttpStatus.OK);

    }

    private String query() {
        try (Cluster cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .build()) {

            Session session = cluster.connect();

            ResultSet rs = session.execute("select release_version from system.local");
            Row row = rs.one();
            return row.getString("release_version");
        }
    }

}
