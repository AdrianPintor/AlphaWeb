package com.alphaeventos.alphaweb.controller;

import com.alphaeventos.alphaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.alphaeventos.alphaweb.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setEnterpriseName(user.getEnterpriseName());
            existingUser.setPersonalName(user.getPersonalName());
            existingUser.setEmail(user.getEmail());
            existingUser.setTelephoneContact(user.getTelephoneContact());
            existingUser.setAddress(user.getAddress());
            existingUser.setRrss(user.getRrss());
            return userRepository.save(existingUser);
        }
        return null;
    }
}