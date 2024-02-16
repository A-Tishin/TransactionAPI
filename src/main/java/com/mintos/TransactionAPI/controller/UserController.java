package com.mintos.TransactionAPI.controller;

import com.mintos.TransactionAPI.exception.EntityNotFoundException;
import com.mintos.TransactionAPI.persistence.entity.UserEntity;
import com.mintos.TransactionAPI.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    UserEntity findUser(@PathVariable Long id) {
        return userService.findUser(id).orElseThrow(()
                -> new EntityNotFoundException("User with ID: " + id + " is not found"));
    }

    @PostMapping
    UserEntity createUser() {
        return userService.create();
    }

}
