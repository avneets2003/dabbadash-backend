package dabbadash.order.repository;

import dabbadash.order.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    @Transactional
    void deleteByCustomer(User customer);

    @Transactional
    void deleteByRestaurant(User restaurant);
}
