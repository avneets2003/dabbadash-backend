package dabbadash.order.controller;

import dabbadash.order.DTO.UserRegistrationDTO;
import dabbadash.order.repository.UserRepository;
import dabbadash.order.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String greet(){
        return "Hey, ATleast the greet API is running (: ";
    }

    @PostMapping("/register")
    private ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO){
        userService.registerUser(userRegistrationDTO);
        return ResponseEntity.ok("User registered successfully!");
    }

}
