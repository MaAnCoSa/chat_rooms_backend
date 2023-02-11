package com.manuelcota.chat_rooms.Controller;

import com.manuelcota.chat_rooms.Model.User;
import com.manuelcota.chat_rooms.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/log")
    public ResponseEntity<?> loginUser(@RequestBody @Valid User user) {
        return userService.loginUser(user);
    }

}
