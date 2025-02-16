package dabbadash.order.controller;

import dabbadash.order.DTO.RegistrationDTO;
import dabbadash.order.service.UserService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        userService.registerUser(registrationDTO);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
