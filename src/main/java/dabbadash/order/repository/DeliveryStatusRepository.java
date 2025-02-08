package dabbadash.order.repository;

import dabbadash.order.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Integer>{

    DeliveryStatus findByDeliveryStatusId(int deliveryStatusId);

}
