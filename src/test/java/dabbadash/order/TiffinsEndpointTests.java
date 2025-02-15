package dabbadash.order;

import dabbadash.order.entity.Tiffin;
import dabbadash.order.entity.User;
// import dabbadash.order.repository.TiffinRepository;
import dabbadash.order.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
// import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TiffinsEndpointTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;
    // @Autowired
    // private TiffinRepository tiffinRepository;

    @SuppressWarnings("null")
    @Test
    void shouldReturnTiffinsForValidRestaurantId() {
        int restaurantId = 1; // Assuming restaurant with ID 1 exists

        // Directly get List<Tiffin>
        ResponseEntity<List<Tiffin>> response = restTemplate.exchange(
                "/tiffins/" + restaurantId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tiffin>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Get list directly
        List<Tiffin> tiffins = response.getBody();

        // Ensure list is not empty
        assertThat(tiffins).isNotNull();

        // Validate each Tiffin has a non-null, non-blank name
        for (Tiffin tiffin : tiffins) {
            assertThat(tiffin.getTiffinName()).isNotBlank();
        }
    }

    @Test
    void shouldReturnNotFoundForNonExistentRestaurant() {
        int restaurantId = 999; // Assuming restaurant ID 999 doesn't exist

        ResponseEntity<List<Tiffin>> response = restTemplate.exchange(
                "/tiffins/" + restaurantId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tiffin>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // If the API returns an empty list instead of 404, check that as well
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldReturnInternalServerErrorOnUnexpectedFailure() {
        int restaurantId = -1; // Assuming negative ID causes a server error

        ResponseEntity<List<Tiffin>> response = restTemplate.exchange(
                "/tiffins/" + restaurantId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tiffin>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        // If API still returns a response body with an error message, check it
        assertThat(response.getBody()).isNull();
    }

    @Test
    void shouldReturnInternalServerErrorIfAnyTiffinIsInvalid() {
        int restaurantId = 1; // Assuming restaurant ID 1 has tiffins

        ResponseEntity<List<Tiffin>> response = restTemplate.exchange(
                "/tiffins/" + restaurantId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Tiffin>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull(); // Assuming API fails and returns no response body
    }

    @Test
    void shouldAddTiffinSuccessfully() {
        // Fetch restaurant from DB
        User restaurant = userRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Tiffin tiffin = new Tiffin();
        tiffin.setTiffinName("Veg Thali");
        tiffin.setTiffinPrice(100);
        tiffin.setTiffinDescription("Delicious veg sabji with 4 roti and dal-rice");
        tiffin.setTiffinImage("https://www.example.com/image.jpg");
        tiffin.setRestaurant(restaurant);


        String url = "/tiffin/" + restaurant.getId();

        ResponseEntity<Void> response = restTemplate.postForEntity(url, tiffin, Void.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldReturnBadRequestWhenTiffinNameIsNotProvided(){
        // Fetch restaurant from DB
        User restaurant = userRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Tiffin incompleteTiffin = new Tiffin();
        incompleteTiffin.setTiffinName(null);
        incompleteTiffin.setTiffinPrice(100);
        incompleteTiffin.setTiffinDescription("Delicious veg sabji with 4 roti and dal-rice");
        incompleteTiffin.setTiffinImage("https://www.example.com/image.jpg");
        incompleteTiffin.setRestaurant(restaurant);

        String url = "/tiffin/" + restaurant.getId();
        ResponseEntity<String> response = restTemplate
                .postForEntity(url, incompleteTiffin, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).contains("Tiffin name is required");
    }

    @Test
    void shouldReturnBadRequestWhenTiffinPriceIsNotProvided(){
        // Fetch restaurant from DB
        User restaurant = userRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Tiffin incompleteTiffin = new Tiffin();
        incompleteTiffin.setTiffinName("Veg Thali");
        incompleteTiffin.setTiffinPrice(0.0);
        incompleteTiffin.setTiffinDescription("Delicious veg sabji with 4 roti and dal-rice");
        incompleteTiffin.setTiffinImage("https://www.example.com/image.jpg");
        incompleteTiffin.setRestaurant(restaurant);

        String url = "/tiffin/" + restaurant.getId();
        ResponseEntity<String> response = restTemplate
                .postForEntity(url, incompleteTiffin, String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).contains("Tiffin price is required and it must be positive number");
    }
}
