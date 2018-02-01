package com.wew.books.repository;

import com.wew.books.repository.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    User findUserByEmailAndPassword(String email, String password);
}
