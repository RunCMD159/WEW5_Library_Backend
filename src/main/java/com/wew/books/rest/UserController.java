package com.wew.books.rest;

import com.wew.books.repository.UserRepository;
import com.wew.books.repository.entities.Book;
import com.wew.books.repository.entities.User;
import com.wew.books.rest.resources.BookResource;
import com.wew.books.rest.resources.UserResource;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void addUsers() {
        for (int i = 0; i < 51; i++) {
            User user = new User();
            user.setUserId(i);
            user.setFirstname("Firstname" + i);
            user.setLastname("Lastname" + i);
            user.setEmail("User@user" + i + ".com");
            user.setPassword("1234567" + i);
            user.setBitcoinWalletPrivateKey(UUID.randomUUID().toString());
            userRepository.save(user);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public ResponseEntity<List<UserResource>> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        List<UserResource> resources = new ArrayList<>();
        users.forEach(user -> resources.add(mapperFacade.map(user, UserResource.class)));
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable int userId) {
        User user = userRepository.findOne(userId);
        UserResource userResource = mapperFacade.map(user, UserResource.class);
        return ResponseEntity.ok(userResource);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/users")
    public ResponseEntity<UserResource> getUserById(@RequestBody UserResource userResource) {
        User user = mapperFacade.map(userResource, User.class);
        userRepository.save(user);
        return ResponseEntity.ok(userResource);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable int userId,
                                                    @RequestBody UserResource userResource) {
        User user = userRepository.findOne(userId);
        User newUser = mapperFacade.map(userResource, User.class);
        //TODO: change user
        return ResponseEntity.ok(userResource);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/books/{isbn}")
    public ResponseEntity<UserResource> deleteBookById(@PathVariable int userId) {
        User user = userRepository.findOne(userId);
        userRepository.delete(userId);
        UserResource resource = mapperFacade.map(user, UserResource.class);
        return ResponseEntity.ok(resource);
    }
}
