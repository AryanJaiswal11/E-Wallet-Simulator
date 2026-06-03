package com.example;

import jakarta.validation.Valid;
import org.hibernate.boot.internal.Abstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public AbstractUser createUser(@RequestBody @Valid UserDTO userdto) {
        return userService.CreateUser(userdto);
    }

    //internalAPI
    @GetMapping("/users/internal/{username}")
    public UserDetails getUser(@PathVariable("username") String username) {
        return this.userService.loadUserByUsername(username);
    }
}
