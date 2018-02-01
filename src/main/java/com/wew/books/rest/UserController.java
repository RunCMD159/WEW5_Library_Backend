package com.wew.books.rest;
import com.wew.books.repository.UserRepository;
import com.wew.books.repository.entities.User;
import com.wew.books.rest.resources.LoginResource;
import com.wew.books.rest.resources.UserResource;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.wew.books.UserRoles.ADMIN;
import static com.wew.books.UserRoles.NORMAL;

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
            user.setRole(NORMAL.name());
            userRepository.save(user);
        }
        User user = new User();
        user.setUserId(-1);
        user.setFirstname("admin");
        user.setLastname("admin");
        user.setEmail("admin@admin.com");
        user.setPassword("admin1234");
        user.setRole(ADMIN.name());
        user.setBitcoinWalletPrivateKey(UUID.randomUUID().toString());
        userRepository.save(user);
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
    public ResponseEntity<UserResource> createNewUser(@RequestBody UserResource userResource) {
        System.out.println(userResource.toString());
        User user = mapperFacade.map(userResource, User.class);
        User dbUser = userRepository.findByEmail(userResource.getEmail());
        if (dbUser != null) {
            throw new RuntimeException("Email already in use");
        }
        userRepository.save(user);
        return ResponseEntity.ok(userResource);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/users/login")
    public ResponseEntity<UserResource> loginUser(@RequestBody LoginResource loginResource) {
        User user = userRepository.findUserByEmailAndPassword(loginResource.getEmail(), loginResource.getPassword());
        if (user == null) {
            throw new RuntimeException("User could not be found");
        }
        UserResource userResource = mapperFacade.map(user, UserResource.class);
        return ResponseEntity.ok(userResource);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/users/{userId}")
    public ResponseEntity<UserResource> changeUserById(@PathVariable int userId,
                                                       @RequestBody UserResource userResource) {
        User usertToUpdate = userRepository.findOne(userId);
         usertToUpdate.setBitcoinWalletPrivateKey(userResource.getBitcoinWalletPrivateKey());
         usertToUpdate.setEmail(userResource.getEmail());
         usertToUpdate.setFirstname(userResource.getEmail());
         usertToUpdate.setLastname(userResource.getLastname());
         usertToUpdate.setPassword(userResource.getPassword());
         usertToUpdate.setRole(userResource.getRole());                                                 
        userRepository.save(usertToUpdate);
        return ResponseEntity.ok(userResource);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/users/{userId}")
    public ResponseEntity<UserResource> deleteUserById(@PathVariable int userId) {
        User user = userRepository.findOne(userId);
        userRepository.delete(userId);
        UserResource resource = mapperFacade.map(user, UserResource.class);
        return ResponseEntity.ok(resource);
    }
}
