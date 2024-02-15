package com.mintos.TransactionAPI.controller;

import com.mintos.TransactionAPI.exception.EntityNotFoundException;
import com.mintos.TransactionAPI.persistence.entity.UserEntity;
import com.mintos.TransactionAPI.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    UserEntity findUser(@PathVariable Long id) {
        return userService.findUser(id).orElseThrow(()
                -> new EntityNotFoundException("User with ID: " + id + " is not found"));
    }

    @PostMapping(value = "/user/create")
    UserEntity createUser() {
        return userService.create();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
