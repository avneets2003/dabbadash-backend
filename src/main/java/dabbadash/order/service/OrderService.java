package dabbadash.order.service;

import dabbadash.order.DTO.RestaurantOrderDTO;
import dabbadash.order.DTO.UserOrderDTO;
import dabbadash.order.entity.Order;
import dabbadash.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<?> getAllOrdersByCustomerId(int customerId) {
        try {
            List<Order> orders = orderRepository.findAllByCustomerId(customerId);

            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            List<UserOrderDTO> userOrderDTOS = orders.stream()
                    .map(order -> new UserOrderDTO(
                            order.getCustomer().getId(),
                            order.getRestaurant().getId(),
                            order.getDeliveryAgent() != null ? order.getDeliveryAgent().getId() : null,
                            order.getStatus(),
                            order.getTotalAmount(),
                            order.getPaymentStatus()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(userOrderDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching order details for customer with ID: "
                            + customerId + ". Message: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getAllOrdersByRestaurantId(int restaurantId) {
        try {
            List<Order> orders = orderRepository.findAllByRestaurantId(restaurantId);

            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            // Have made new RestaurantOrderDTO, because don't want to expose usedId to restaurants
            List<RestaurantOrderDTO> restaurantOrderDTOS = orders.stream()
                    .map(order -> new RestaurantOrderDTO(
                            order.getRestaurant().getId(),
                            order.getDeliveryAgent() != null ? order.getDeliveryAgent().getId() : null,
                            order.getStatus(),
                            order.getTotalAmount(),
                            order.getPaymentStatus()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(restaurantOrderDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while fetching order details for restaurant with ID: "
                            + restaurantId + ". Message: " + e.getMessage());
        }
    }

}
