package com.example.url_shortener.User;


import com.example.url_shortener.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<User>> registerUser(@RequestBody User user) {

        User newUser = userService.createUser(user);

        SuccessResponse<User> response = new SuccessResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "User created successfully",
                newUser

        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


}
