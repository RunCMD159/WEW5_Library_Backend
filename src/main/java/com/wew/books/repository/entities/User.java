package com.wew.books.repository.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    private String userId;

    @Id
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
