package dabbadash.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dabbadash.order.DTO.LoginResponse;
import dabbadash.order.entity.User;
import dabbadash.order.service.AuthService;

@RestController
public class LoginController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        String jwt = authService.authenticate(user.getEmail(), user.getUserPassword());
        LoginResponse response = new LoginResponse("Login successful", "Bearer " + jwt);
        return ResponseEntity.ok(response);
    }
}
