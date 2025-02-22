package dabbadash.order.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantOrderDTO {
    private int restaurantId;
    private Integer deliveryAgentId; // allows null
    private String status;
    private int totalAmount;
    private String paymentStatus;
}
