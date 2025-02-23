package dabbadash.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dabbadash.order.DTO.LoginResponse;
import dabbadash.order.entity.User;
import dabbadash.order.service.AuthService;

@RestController
@Service
public class LoginController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        String jwt = authService.authenticate(user.getUserEmail(), user.getUserPassword());
        LoginResponse response = new LoginResponse("Login successful", "Bearer " + jwt, user.getId(), user.getUserRole());
        return ResponseEntity.ok(response);
    }
}
