package com.wew.books.repository.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {
    private String isbn;

    @Id
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
