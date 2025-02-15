package dabbadash.order.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String userPassword;
    private String fullName;
    private String phoneNumber;
    private String userAddress;
    private String userRole;
}
