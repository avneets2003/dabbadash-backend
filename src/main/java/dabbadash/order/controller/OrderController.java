package dabbadash.order.controller;

import dabbadash.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getOrdersByCustomerId(@PathVariable int customerId) {
        // If no error than you will get List, else you will get String
        return orderService.getAllOrdersByCustomerId(customerId);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<?> getOrdersByRestaurantId(@PathVariable int restaurantId) {
        // If no error than you will get List, else you will get String
        return orderService.getAllOrdersByRestaurantId(restaurantId);
    }


}
