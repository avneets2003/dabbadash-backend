package dabbadash.order.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TiffinDTO {
    private int id;
    private String tiffinName;
    private double price;
    private String tiffinDescription;
    private String tiffinImage;
    private String category;
    private int restaurantId; // Extracted from the entity's `restaurant` field
}
