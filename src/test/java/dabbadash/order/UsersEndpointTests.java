package dabbadash.order;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import dabbadash.order.DTO.LoginResponse;
import dabbadash.order.entity.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetUsersEndpointTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private TestRestTemplate adminRestTemplate;
    private TestRestTemplate customerRestTemplate;

    private Integer adminId;
    private Integer customerId;

    @BeforeEach
    void setup(@Autowired RestTemplateBuilder restTemplateBuilder) {
        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setUserPassword("password123");
        admin.setFullName("Admin");
        admin.setPhoneNumber("9998887777");
        admin.setUserAddress("123 Admin Street");
        admin.setUserRole("admin");

        restTemplate.postForEntity("/register", admin, Void.class);
        LoginResponse adminLoginResponse = loginAndGetJwt("admin@example.com", "password123");
        adminId = adminLoginResponse.getUserId();
        String adminJwt = adminLoginResponse.getToken();

        User customer = new User();
        customer.setEmail("customer@example.com");
        customer.setUserPassword("password456");
        customer.setFullName("Customer");
        customer.setPhoneNumber("8887776666");
        customer.setUserAddress("456 User Street");
        customer.setUserRole("customer");

        restTemplate.postForEntity("/register", customer, Void.class);
        LoginResponse customerLoginResponse = loginAndGetJwt("customer@example.com", "password456");
        customerId = customerLoginResponse.getUserId();
        String customerJwt = customerLoginResponse.getToken();
        
        adminRestTemplate = new TestRestTemplate(
            restTemplateBuilder.defaultHeader("Authorization", "Bearer " + adminJwt));
        
        customerRestTemplate = new TestRestTemplate(
            restTemplateBuilder.defaultHeader("Authorization", "Bearer " + customerJwt));
    }

    private LoginResponse loginAndGetJwt(String email, String password) {
        User loginUser = new User();
        loginUser.setEmail(email);
        loginUser.setUserPassword(password);

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/login", loginUser, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    @Test
    void shouldReturnUsersListForAdmin() {
        ResponseEntity<String> response = adminRestTemplate.getForEntity("/users", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("[");
    }

    @Test
    void shouldReturnForbiddenForNonAdminUser() {
        ResponseEntity<String> response = customerRestTemplate.getForEntity("/users", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("Access denied");
    }

    @Test
    void shouldReturnUnauthorizedWhenNoJwtProvided() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).contains("JWT token is missing");
    }

    @Test
    void userCanGetOwnInfo() {
        ResponseEntity<String> response = customerRestTemplate.getForEntity("/users/" + customerId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("customer@example.com");
    }

    @Test
    void adminCanGetAnyUserInfo() {
        ResponseEntity<String> response = adminRestTemplate.getForEntity("/users/" + customerId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void userCannotGetOtherUserInfo() {
        ResponseEntity<String> response = customerRestTemplate.getForEntity("/users/" + adminId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void userCanUpdateOwnInfo() {
        User updateData = new User();
        updateData.setEmail("customer@example.com");
        updateData.setUserPassword("newpassword");
        updateData.setFullName("Updated Customer");
        updateData.setPhoneNumber("1112223333");
        updateData.setUserAddress("Updated Address");
        updateData.setUserRole("customer");
        ResponseEntity<Void> response = customerRestTemplate.exchange("/users/" + customerId, HttpMethod.PUT, new HttpEntity<>(updateData), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void userCannotUpdateOwnRole() {
        User updateData = new User();
        updateData.setEmail("customer@example.com");
        updateData.setUserPassword("password456");
        updateData.setFullName("Customer");
        updateData.setPhoneNumber("8887776666");
        updateData.setUserAddress("456 User Street");
        updateData.setUserRole("admin");
        ResponseEntity<Void> response = customerRestTemplate.exchange("/users/" + customerId, HttpMethod.PUT, new HttpEntity<>(updateData), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void adminCanUpdateAnyUserInfo() {
        User updateData = new User();
        updateData.setEmail("customer@example.com");
        updateData.setUserPassword("password456");
        updateData.setFullName("Updated Customer");
        updateData.setPhoneNumber("8887776666");
        updateData.setUserAddress("Updated Address");
        updateData.setUserRole("customer");
        ResponseEntity<Void> response = adminRestTemplate.exchange("/users/" + customerId, HttpMethod.PUT, new HttpEntity<>(updateData), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void userCannotDeleteOtherUser() {
        ResponseEntity<Void> response = customerRestTemplate.exchange("/users/" + adminId, HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void adminCanDeleteAnyUser() {
        ResponseEntity<Void> response = adminRestTemplate.exchange("/users/" + customerId, HttpMethod.DELETE, null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void unauthorizedUserCannotAccessUserInfo() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users/" + customerId, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
