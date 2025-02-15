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

    public String authenticate(String email, String password) {
        validateUser(email, password);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getUserPassword())) {
                return generateJwtToken(user.getEmail());
            } else {
                throw new UnauthorizedException("Invalid credentials");
            }
        } else {
            throw new UnauthorizedException("Email does not exist");
        }
    }

    private String generateJwtToken(String email) {
        long expirationTime = 1000 * 60 * 60;
        return Jwts.builder()
            .subject(email)
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), Jwts.SIG.HS512)
            .compact();
    }

    private void validateUser(String email, String password) {
        if (email == null) {
            throw new BadRequestException("Email is required");
        }

        if (password == null) {
            throw new BadRequestException("Password is required");
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            throw new BadRequestException("Invalid email format");
        }
    }
}
