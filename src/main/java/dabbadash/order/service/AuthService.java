package dabbadash.order.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import dabbadash.order.entity.User;
import dabbadash.order.exception.BadRequestException;
import dabbadash.order.exception.UnauthorizedException;
import dabbadash.order.repository.UserRepository;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String userEmail, String password) {
        validateUser(userEmail, password);
        Optional<User> optionalUser = userRepository.findByuserEmail(userEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getUserPassword())) {
                return generateJwtToken(user.getUserEmail());
            } else {
                throw new UnauthorizedException("Invalid credentials");
            }
        } else {
            throw new UnauthorizedException("userEmail does not exist");
        }
    }

    private String generateJwtToken(String userEmail) {
        long expirationTime = 1000 * 60 * 60;
        return Jwts.builder()
            .subject(userEmail)
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), Jwts.SIG.HS512)
            .compact();
    }

    private void validateUser(String userEmail, String password) {
        if (userEmail == null) {
            throw new BadRequestException("userEmail is required");
        }

        if (password == null) {
            throw new BadRequestException("Password is required");
        }

        String userEmailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!userEmail.matches(userEmailRegex)) {
            throw new BadRequestException("Invalid userEmail format");
        }
    }
}
