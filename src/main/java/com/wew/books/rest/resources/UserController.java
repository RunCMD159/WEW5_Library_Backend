package com.wew.books.rest.resources;

import com.wew.books.repository.entities.User;
import com.wew.books.repository.entities.UserRepository;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public ResponseEntity<List<UserResource>> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        List<UserResource> resources = new ArrayList<>();
        users.forEach(user -> resources.add(mapperFacade.map(user, UserResource.class)));
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable String userId) {
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
    public ResponseEntity<UserResource> getUserById(@PathVariable String userId,
                                                    @RequestBody UserResource userResource) {
        User user = userRepository.findOne(userId);
        User newUser = mapperFacade.map(userResource, User.class);
        //TODO: change user
        return ResponseEntity.ok(userResource);
    }
}
