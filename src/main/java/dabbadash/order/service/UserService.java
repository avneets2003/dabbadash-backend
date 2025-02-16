package dabbadash.order.service;

import dabbadash.order.repository.UserRepository;
import dabbadash.order.DTO.RegistrationDTO;
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
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegistrationDTO registrationDTO) {
        validateUser(registrationDTO);

        User user = new User();
        user.setUserEmail(registrationDTO.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(registrationDTO.getUserPassword()));
        user.setUserName(registrationDTO.getUserName());
        user.setUserPhoneNumber(registrationDTO.getUserPhoneNumber());
        user.setUserAddress(registrationDTO.getUserAddress());
        user.setUserRole(registrationDTO.getUserRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public void validateUser(RegistrationDTO registrationDTO) {
        if (registrationDTO.getUserEmail() == null || registrationDTO.getUserEmail().isEmpty()) {
            throw new BadRequestException("userEmail is required");
        }

        String userEmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!registrationDTO.getUserEmail().matches(userEmailRegex)) {
            throw new BadRequestException("Invalid userEmail format");
        }

        if (registrationDTO.getUserPhoneNumber() == null || registrationDTO.getUserPhoneNumber().isEmpty()) {
            throw new BadRequestException("Contact number is required");
        }

        if (registrationDTO.getUserPhoneNumber().length() != 10) {
            throw new BadRequestException("Contact number must be 10 digits");
        }

        String userPhoneNumberRegex = "^[0-9]{10}$";
        if (!registrationDTO.getUserPhoneNumber().matches(userPhoneNumberRegex)) {
            throw new BadRequestException("Invalid contact number format");
        }

        if (registrationDTO.getUserPassword() == null || registrationDTO.getUserPassword().isEmpty()) {
            throw new BadRequestException("Password is required");
        }

        if (userRepository.findByuserEmail(registrationDTO.getUserEmail()).isPresent()) {
            throw new BadRequestException("userEmail already in use");
        }
        
        if (userRepository.findByuserPhoneNumber(registrationDTO.getUserPhoneNumber()).isPresent()) {
            throw new BadRequestException("Phone number already in use");
        }
    }
}
