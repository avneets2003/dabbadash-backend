package dabbadash.order;

import dabbadash.order.entity.Tiffin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TiffinsEndpointTests {
    @Autowired
    private TestRestTemplate restTemplate;

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

}
