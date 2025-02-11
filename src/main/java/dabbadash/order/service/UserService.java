package dabbadash.order.service;

import dabbadash.order.repository.UserRepository;
import dabbadash.order.DTO.UserDTO;
import dabbadash.order.entity.User;
import dabbadash.order.exception.BadRequestException;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserDTO userDTO) {
        validateUser(userDTO);

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserAddress(userDTO.getUserAddress());
        user.setUserRole(userDTO.getUserRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    private void validateUser(UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required.");
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!userDTO.getEmail().matches(emailRegex)) {
            throw new BadRequestException("Invalid email format.");
        }
        
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new BadRequestException("Email already in use.");
        }
        if (userRepository.findByPhoneNumber(userDTO.getPhoneNumber()).isPresent()) {
            throw new BadRequestException("Phone number already in use.");
        }
    }
}
