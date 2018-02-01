package com.wew.books.rest;

import com.wew.books.repository.BookRepository;
import com.wew.books.repository.entities.Book;
import com.wew.books.rest.resources.BookResource;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
public class BooksController {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private BookRepository bookRepository;

    @PostConstruct
    private void addBooks() {
        Random random = new Random();
        for (int i = 0; i < 51; i++) {
            Book book = new Book();
            book.setIsbn(UUID.randomUUID().toString());
            book.setAuthor("Author" + i);
            book.setPages(random.nextInt(1000));
            if (random.nextInt(2) == 1) {
                book.setAvailable(false);
                book.setReturnDate(new Date().toString());
            } else {
                book.setAvailable(true);
            }
            book.setTitle("Book" + i);
            bookRepository.save(book);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/books")
    public ResponseEntity<List<BookResource>> getAllBooks() {
        List<Book> books = (List<Book>) bookRepository.findAll();
        List<BookResource> resources = new ArrayList<>();
        books.forEach(user -> resources.add(mapperFacade.map(user, BookResource.class)));
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/books/{isbn}")
    public ResponseEntity<BookResource> getBookByIsbn(@PathVariable String isbn) {
        Book book = bookRepository.findOne(isbn);
        BookResource bookResource = mapperFacade.map(book, BookResource.class);
        return ResponseEntity.ok(bookResource);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/books")
    public ResponseEntity<BookResource> createNewBook(@RequestBody BookResource bookResource) {
        Book book = mapperFacade.map(bookResource, Book.class);
        bookRepository.save(book);
        return ResponseEntity.ok(bookResource);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/books/{isbn}")
    public ResponseEntity<BookResource> changeBook(@PathVariable String isbn,
                                                   @RequestBody BookResource bookResource) {
        Book book = bookRepository.findOne(isbn);
        Book newBook = mapperFacade.map(bookResource, Book.class);
        //TODO: change user
        return ResponseEntity.ok(bookResource);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/books/{isbn}")
    public ResponseEntity<BookResource> deleteBookById(@PathVariable String isbn) {
        Book book = bookRepository.findOne(isbn);
        bookRepository.delete(isbn);
        BookResource resource = mapperFacade.map(book, BookResource.class);
        return ResponseEntity.ok(resource);
    }
}
