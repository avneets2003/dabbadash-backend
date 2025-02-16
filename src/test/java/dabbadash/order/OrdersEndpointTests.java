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

import dabbadash.order.DTO.LoginRequest;
import dabbadash.order.DTO.LoginResponse;
import dabbadash.order.DTO.OrderDTO;
import dabbadash.order.DTO.RegistrationDTO;
import dabbadash.order.entity.Order;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrdersEndpointTests {
	@Autowired
	private TestRestTemplate restTemplate;

	private TestRestTemplate customerRestTemplate;
	private TestRestTemplate restaurantRestTemplate;
	private TestRestTemplate deliveryAgentRestTemplate;
	// private TestRestTemplate adminRestTemplate;

	private int customerId;
	private int restaurantId;
	private int deliveryAgentId;
	// private String adminId;

	@BeforeEach
	void setup(@Autowired RestTemplateBuilder restTemplateBuilder) {
		RegistrationDTO customer = new RegistrationDTO("customer@example.com", "password123", "Customer", "9998887777", "123 User Street", "customer");
        restTemplate.postForEntity("/register", customer, Void.class);
		LoginResponse customerLoginResponse = loginAndGetJwt("customer@example.com", "password123");
		customerId = customerLoginResponse.getUserId();
		String customerJwt = customerLoginResponse.getToken();
		customerRestTemplate = new TestRestTemplate(restTemplateBuilder.defaultHeader("Authorization", "Bearer " + customerJwt));

		RegistrationDTO restaurant = new RegistrationDTO("restaurant@example.com", "password456", "Restaurant", "8887776666", "456 User Street", "restaurant");
        restTemplate.postForEntity("/register", restaurant, Void.class);
		LoginResponse restaurantLoginResponse = loginAndGetJwt("restaurant@example.com", "password456");
		restaurantId = restaurantLoginResponse.getUserId();
		String restaurantJwt = restaurantLoginResponse.getToken();
		restaurantRestTemplate = new TestRestTemplate(restTemplateBuilder.defaultHeader("Authorization", "Bearer " + restaurantJwt));

		RegistrationDTO deliveryAgent = new RegistrationDTO("delivery_agent@example.com", "password789", "Delivery Agent", "7776665555", "789 User Street", "delivery_agent");
        restTemplate.postForEntity("/register", deliveryAgent, Void.class);
		LoginResponse deliveryAgentLoginResponse = loginAndGetJwt("delivery_agent@example.com", "password789");
		deliveryAgentId = deliveryAgentLoginResponse.getUserId();
		String deliveryAgentJwt = deliveryAgentLoginResponse.getToken();
		deliveryAgentRestTemplate = new TestRestTemplate(restTemplateBuilder.defaultHeader("Authorization", "Bearer " + deliveryAgentJwt));

		// UserDTO admin = new UserDTO("admin@example.com", "admin-password", "Admin", "6665554444", "123 Admin Street", "admin");
		// restTemplate.postForEntity("/register", admin, Void.class);
		// LoginResponse adminLoginResponse = loginAndGetJwt("admin@example.com", "admin-password");
		// adminId = adminLoginResponse.getUserId();
		// String adminJwt = adminLoginResponse.getToken();
		// adminRestTemplate = new TestRestTemplate(restTemplateBuilder.defaultHeader("Authorization", "Bearer " + adminJwt));
	}

	private LoginResponse loginAndGetJwt(String userEmail, String password) {
        LoginRequest loginRequest = new LoginRequest(userEmail, password);

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/login", loginRequest, LoginResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

	@Test
    void shouldReturnOrderListForCustomer() {
        ResponseEntity<Order[]> response = customerRestTemplate.getForEntity("/orders/" + customerId, Order[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    void shouldCreateOrderForCustomer() {
        OrderDTO newOrder = new OrderDTO(customerId, restaurantId, null, "pending", 100, "pending");
        ResponseEntity<Order> response = customerRestTemplate.postForEntity("/orders/" + customerId, newOrder, Order.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
    
    @Test
    void shouldReturnUnauthorizedWhenNoJwtProvided() {
        ResponseEntity<Order[]> response = restTemplate.getForEntity("/orders/" + customerId, Order[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    
    @Test
    void shouldReturnOrderListForRestaurant() {
        ResponseEntity<Order[]> response = restaurantRestTemplate.getForEntity("/orders/" + restaurantId, Order[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    void shouldUpdateOrderForRestaurant() {
        OrderDTO updateOrder = new OrderDTO(customerId, restaurantId, null, "accepted", 100, "completed");
        ResponseEntity<Void> response = restaurantRestTemplate.exchange("/orders/" + restaurantId, HttpMethod.PUT, new HttpEntity<>(updateOrder), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    void shouldReturnOrderListForDeliveryAgent() {
        ResponseEntity<Order[]> response = deliveryAgentRestTemplate.getForEntity("/orders/" + deliveryAgentId, Order[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    void shouldUpdateOrderForDeliveryAgent() {
        OrderDTO updateOrder = new OrderDTO(customerId, restaurantId, deliveryAgentId, "in_transit", 100, "completed");
        ResponseEntity<Void> response = deliveryAgentRestTemplate.exchange("/orders/" + deliveryAgentId, HttpMethod.PUT, new HttpEntity<>(updateOrder), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
