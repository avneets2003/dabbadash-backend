package dabbadash.order.service;

import dabbadash.order.DTO.UserRegistrationDTO;
import dabbadash.order.entity.User;
import dabbadash.order.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDTO userRegistrationDTO){
        if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }
        if (userRepository.findByPhoneNumber(userRegistrationDTO.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Phone number already in use.");
        }

        if (!userRegistrationDTO.getUserConfirmPassword().equals(userRegistrationDTO.getUserPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        User user = new User();
        user.setEmail(userRegistrationDTO.getEmail());
        user.setUserPassword(passwordEncoder.encode(userRegistrationDTO.getUserPassword())); // Encrypt password
        user.setFullName(userRegistrationDTO.getFullName());
        user.setPhoneNumber(userRegistrationDTO.getPhoneNumber());
        user.setUserAddress(userRegistrationDTO.getUserAddress());
        user.setUserRole(userRegistrationDTO.getUserRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

}
