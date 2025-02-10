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
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            return new ResponseEntity<>("Email is required.", HttpStatus.BAD_REQUEST);
        }
    
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!userDTO.getEmail().matches(emailRegex)) {
            return new ResponseEntity<>("Invalid email format.", HttpStatus.BAD_REQUEST);
        }
    
        userService.registerUser(userDTO);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
