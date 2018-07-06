package com.flockse.platformbackend.repositories;

import com.flockse.platformbackend.domain.Book;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CassandraRepository<Book, String> {
}
