package dabbadash.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
    OrderItems findById(int id);
    OrderItems findByOrder_Id(int orderId);
    OrderItems findByTiffin_Id(int tiffinId);
}
