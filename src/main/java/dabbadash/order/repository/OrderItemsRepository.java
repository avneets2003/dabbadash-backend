package dabbadash.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
    OrderItems findByOrderItemId(int orderItemId);
    OrderItems findByOrder_OrderId(int orderId);
    OrderItems findByTiffin_TiffinId(int tiffinId);
}
