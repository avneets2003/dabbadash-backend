package dabbadash.order.repository;

import dabbadash.order.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    @Transactional
    void deleteByCustomer(User customer);

    @Transactional
    void deleteByRestaurant(User restaurant);

    Optional<Order> findById(int id);
    List<Order> findAllByCustomerId(int customerId);

    List<Order> findAllByRestaurantId(int restaurantId);

    List<Order> findAllByDeliveryAgentId(int deliveryAgentId);

}
