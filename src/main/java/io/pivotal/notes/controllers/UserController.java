package io.pivotal.notes.controllers;

import io.pivotal.notes.models.User;
import io.pivotal.notes.models.UserRequest;
import io.pivotal.notes.models.UserResponse;
import io.pivotal.notes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.StringUtils.*;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserRepository userRepository;

    @Autowired
    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> saveOrUpdateUser(@RequestBody UserRequest user) {
        if (valid(user)) {
            User savedUser = userRepository.saveOrUpdateUser(user.getId(), user.getUsername(), user.getPassword());
            UserResponse response = new UserResponse();
            response.setId(savedUser.getId());
            response.setUsername(savedUser.getUsername());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "user/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        User user = userRepository.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean valid(UserRequest user) {
        return !isEmpty(user.getUsername()) && !isEmpty(user.getPassword());
    }
}
