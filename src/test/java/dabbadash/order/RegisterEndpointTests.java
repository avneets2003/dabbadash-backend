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
        newUser.setUserEmail("alice.smith@example.com");
        newUser.setUserPassword("password123");
        newUser.setUserName("Alice Smith");
        newUser.setUserPhoneNumber("1112223333");
        newUser.setUserAddress("456 Oak Street");
        newUser.setUserRole("customer");
        ResponseEntity<Void> response = restTemplate
            .postForEntity("/register", newUser, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnBadRequestWhenuserEmailIsNotProvided() {
        User incompleteUser = new User();
        incompleteUser.setUserEmail(null);
        incompleteUser.setUserPassword("password456");
        incompleteUser.setUserName("Bob Jones");
        incompleteUser.setUserPhoneNumber("4445556666");
        incompleteUser.setUserAddress("789 Pine Avenue");
        incompleteUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", incompleteUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("userEmail is required");
    }

    @Test
    void shouldReturnBadRequestWhenuserEmailHasInvalidFormat() {
        User invalidUser = new User();
        invalidUser.setUserEmail("invalid-userEmail");
        invalidUser.setUserPassword("password456");
        invalidUser.setUserName("Bob Jones");
        invalidUser.setUserPhoneNumber("4445556666");
        invalidUser.setUserAddress("789 Pine Avenue");
        invalidUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", invalidUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid userEmail format");
    }

    @Test
    void shouldReturnBadRequestWhenuserPhoneNumberIsNotProvided() {
        User incompleteUser = new User();
        incompleteUser.setUserEmail("bob.jones@example.com");
        incompleteUser.setUserPassword("password456");
        incompleteUser.setUserName("Bob Jones");
        incompleteUser.setUserPhoneNumber(null);
        incompleteUser.setUserAddress("789 Pine Avenue");
        incompleteUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", incompleteUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Contact number is required");
    }

    @Test
    void shouldReturnBadRequestWhenuserPhoneNumberIsTooShort() {
        User invalidUser = new User();
        invalidUser.setUserEmail("bob.jones@example.com");
        invalidUser.setUserPassword("password456");
        invalidUser.setUserName("Bob Jones");
        invalidUser.setUserPhoneNumber("444555666");
        invalidUser.setUserAddress("789 Pine Avenue");
        invalidUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", invalidUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Contact number must be 10 digits");
    }

    @Test
    void shouldReturnBadRequestWhenuserPhoneNumberHasInvalidFormat() {
        User invalidUser = new User();
        invalidUser.setUserEmail("bob.jones@example.com");
        invalidUser.setUserPassword("password456");
        invalidUser.setUserName("Bob Jones");
        invalidUser.setUserPhoneNumber("number1234");
        invalidUser.setUserAddress("789 Pine Avenue");
        invalidUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", invalidUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid contact number format");
    }

    @Test
    void shouldReturnBadRequestWhenPasswordIsNotProvided() {
        User invalidUser = new User();
        invalidUser.setUserEmail("bob.jones@example.com");
        invalidUser.setUserPassword(null);
        invalidUser.setUserName("Bob Jones");
        invalidUser.setUserPhoneNumber("4445556666");
        invalidUser.setUserAddress("789 Pine Avenue");
        invalidUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", invalidUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Password is required");
    }

    @Test
    void shouldReturnBadRequestIfuserEmailIsAlreadyInUse() {
        User newUser = new User();
        newUser.setUserEmail("alice.smith@example.com");
        newUser.setUserPassword("password123");
        newUser.setUserName("Alice Smith");
        newUser.setUserPhoneNumber("9999888777");
        newUser.setUserAddress("456 Oak Street");
        newUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", newUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("userEmail already in use");
    }

    @Test
    void shouldReturnBadRequestIfuserPhoneNumberIsAlreadyInUse() {
        User newUser = new User();
        newUser.setUserEmail("smith.alice@example.com");
        newUser.setUserPassword("password123");
        newUser.setUserName("Alice Smith");
        newUser.setUserPhoneNumber("1112223333");
        newUser.setUserAddress("456 Oak Street");
        newUser.setUserRole("customer");
        ResponseEntity<String> response = restTemplate
            .postForEntity("/register", newUser, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Phone number already in use");
    }
}