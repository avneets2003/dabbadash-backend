package dabbadash.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import dabbadash.order.entity.User;
import dabbadash.order.DTO.LoginResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginEndpointTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @BeforeEach
    void createUser() {
        User newUser = new User();
        newUser.setEmail("alice.smith@example.com");
        newUser.setUserPassword("password123");
        newUser.setFullName("Alice Smith");
        newUser.setPhoneNumber("1112223333");
        newUser.setUserAddress("456 Oak Street");
        newUser.setUserRole("customer");
        restTemplate.postForEntity("/register", newUser, Void.class);
    }

    @Test
    void shouldLoginUserSuccessfully() {
        User loginUser = new User();
        loginUser.setEmail("alice.smith@example.com");
        loginUser.setUserPassword("password123");

        ResponseEntity<LoginResponse> response = restTemplate
            .postForEntity("/login", loginUser, LoginResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Login successful");
        assertThat(response.getBody().getToken()).startsWith("Bearer ");
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsMissing() {
        User loginUser = new User();
        loginUser.setEmail(null);
        loginUser.setUserPassword("password123");

        ResponseEntity<String> response = restTemplate
            .postForEntity("/login", loginUser, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email is required");
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsMissing() {
        User loginUser = new User();
        loginUser.setEmail("alice.smith@example.com");
        loginUser.setUserPassword(null);

        ResponseEntity<String> response = restTemplate
            .postForEntity("/login", loginUser, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Password is required");
    }

    @Test
    void shouldReturnBadRequestWhenInvalidEmailFormat() {
        User loginUser = new User();
        loginUser.setEmail("invalid-email");
        loginUser.setUserPassword("password123");

        ResponseEntity<String> response = restTemplate
            .postForEntity("/login", loginUser, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Invalid email format");
    }

    @Test
    void shouldReturnUnauthorizedWhenIncorrectPassword() {
        User loginUser = new User();
        loginUser.setEmail("alice.smith@example.com");
        loginUser.setUserPassword("incorrectpassword");

        ResponseEntity<String> response = restTemplate
            .postForEntity("/login", loginUser, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("Invalid credentials");
    }

    @Test
    void shouldReturnUnauthorizedWhenEmailDoesNotExist() {
        User loginUser = new User();
        loginUser.setEmail("nonexistent@example.com");
        loginUser.setUserPassword("password123");

        ResponseEntity<String> response = restTemplate
            .postForEntity("/login", loginUser, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("Email does not exist");
    }
}
