package dabbadash.order.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int customerId;
    private int restaurantId;
    private Integer deliveryAgentId; // allows null
    private String status;
    private int totalAmount;
    private String paymentStatus;
}
