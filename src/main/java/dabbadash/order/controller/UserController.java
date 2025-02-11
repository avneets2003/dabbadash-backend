package dabbadash.order.controller;

import dabbadash.order.DTO.UserDTO;
import dabbadash.order.service.UserService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/register")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
