package dabbadash.order;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import dabbadash.order.entity.User;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterEndpointTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldRegisterUserSuccessfully() {
        User newUser = new User(123, "john.doe@example.com", "password123", "John Doe", "1234567890", "123 Main Street", User.Role.CUSTOMER, LocalDateTime.now(), LocalDateTime.now());
        ResponseEntity<Void> response = restTemplate
            .postForEntity("/register", newUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnBadRequestWhenUserInformationIsIncomplete() {
        User incompleteUser = new User(123, null, "password123", "Jane Doe", "1234567890", "123 Main Street", User.Role.CUSTOMER, LocalDateTime.now(), LocalDateTime.now());
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", incompleteUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email is required");
    }

    @Test
    void shouldReturnBadRequestWhenUserInformationIsInvalid() {
        User invalidUser = new User(123, "invalid-email", "password123", "Invalid User", "1234567890", "123 Main Street", User.Role.CUSTOMER, LocalDateTime.now(), LocalDateTime.now());
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", invalidUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid email format");
    }
}