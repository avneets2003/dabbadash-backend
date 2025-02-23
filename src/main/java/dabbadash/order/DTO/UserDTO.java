package dabbadash.order.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String userEmail;
    private String userName;
    private String userPhoneNumber;
    private String userAddress;
    private String userRole;
}
