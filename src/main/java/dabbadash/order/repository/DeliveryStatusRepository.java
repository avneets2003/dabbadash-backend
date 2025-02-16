package dabbadash.order.repository;

import dabbadash.order.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.DeliveryStatus;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Integer>{
    DeliveryStatus findById(int id);
    @Transactional
    void deleteByDeliveryAgent(User restaurant);
}
