package com.wew.books.rest;

import com.wew.books.repository.BookRepository;
import com.wew.books.repository.entities.Book;
import com.wew.books.rest.resources.BookResource;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BooksController {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/books")
    public ResponseEntity<List<BookResource>> getAllBooks() {
        List<Book> books = (List<Book>) bookRepository.findAll();
        List<BookResource> resources = new ArrayList<>();
        books.forEach(user -> resources.add(mapperFacade.map(user, BookResource.class)));
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/books/{isbn}")
    public ResponseEntity<BookResource> getUserById(@PathVariable String isbn) {
        Book book = bookRepository.findOne(isbn);
        BookResource bookResource = mapperFacade.map(book, BookResource.class);
        return ResponseEntity.ok(bookResource);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/books")
    public ResponseEntity<BookResource> getUserById(@RequestBody BookResource bookResource) {
        Book book = mapperFacade.map(bookResource, Book.class);
        bookRepository.save(book);
        return ResponseEntity.ok(bookResource);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/books/{isbn}")
    public ResponseEntity<BookResource> getUserById(@PathVariable String isbn,
                                                    @RequestBody BookResource bookResource) {
        Book book = bookRepository.findOne(isbn);
        Book newBook = mapperFacade.map(bookResource, Book.class);
        //TODO: change user
        return ResponseEntity.ok(bookResource);
    }
}
