package dabbadash.order.repository;

import dabbadash.order.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
    OrderItems findByOrderItemsId(int orderItemsId);
    OrderItems findByOrderId(int orderId);
    OrderItems findByTiffinId(int tiffinId);
}
