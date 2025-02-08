package dabbadash.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
    OrderItems findByOrderItemsId(int orderItemsId);
    OrderItems findByOrderId(int orderId);
    OrderItems findByTiffinId(int tiffinId);
}
