package dabbadash.order;
import dabbadash.order.entity.User;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

// import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterEndpointTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldRegisterUserSuccessfully() {
        User newUser = new User();
        newUser.setEmail("john.doe@example.com");
        newUser.setUserPassword("password123");
        newUser.setFullName("John Doe");
        newUser.setPhoneNumber("1234567890");
        newUser.setUserAddress("123 Main Street");
        newUser.setUserRole("customer");
        ResponseEntity<Void> response = restTemplate
            .postForEntity("/register", newUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnBadRequestWhenUserInformationIsIncomplete() {
        User incompleteUser = new User();
        incompleteUser.setEmail(null);
        incompleteUser.setUserPassword("password123");
        incompleteUser.setFullName("John Doe");
        incompleteUser.setPhoneNumber("1234567890");
        incompleteUser.setUserAddress("123 Main Street");
        incompleteUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", incompleteUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email is required");
    }

    @Test
    void shouldReturnBadRequestWhenUserInformationIsInvalid() {
        User invalidUser = new User();
        invalidUser.setEmail("invalid-email");
        invalidUser.setUserPassword("password123");
        invalidUser.setFullName("John Doe");
        invalidUser.setPhoneNumber("1234567890");
        invalidUser.setUserAddress("123 Main Street");
        invalidUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", invalidUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid email format");
    }
}