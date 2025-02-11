package dabbadash.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import dabbadash.order.entity.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterEndpointTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldRegisterUserSuccessfully() {
        User newUser = new User();
        newUser.setEmail("alice.smith@example.com");
        newUser.setUserPassword("password123");
        newUser.setFullName("Alice Smith");
        newUser.setPhoneNumber("1112223333");
        newUser.setUserAddress("456 Oak Street");
        newUser.setUserRole("customer");
        ResponseEntity<Void> response = restTemplate
            .postForEntity("/register", newUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnBadRequestWhenUserInformationIsIncomplete() {
        User incompleteUser = new User();
        incompleteUser.setEmail(null);
        incompleteUser.setUserPassword("password456");
        incompleteUser.setFullName("Bob Jones");
        incompleteUser.setPhoneNumber("4445556666");
        incompleteUser.setUserAddress("789 Pine Avenue");
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
        invalidUser.setUserPassword("password789");
        invalidUser.setFullName("Carol White");
        invalidUser.setPhoneNumber("7778889999");
        invalidUser.setUserAddress("101 Maple Road");
        invalidUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", invalidUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid email format");
    }

    @Test
    void shouldReturnBadRequestIfEmailIsAlreadyInUse() {
        User newUser = new User();
        newUser.setEmail("alice.smith@example.com");
        newUser.setUserPassword("password123");
        newUser.setFullName("Alice Smith");
        newUser.setPhoneNumber("9999888777");
        newUser.setUserAddress("456 Oak Street");
        newUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", newUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email already in use.");
    }

    @Test
    void shouldReturnBadRequestIfPhoneNumberIsAlreadyInUse() {
        User newUser = new User();
        newUser.setEmail("smith.alice@example.com");
        newUser.setUserPassword("password123");
        newUser.setFullName("Alice Smith");
        newUser.setPhoneNumber("1112223333");
        newUser.setUserAddress("456 Oak Street");
        newUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", newUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Phone number already in use.");
    }
}