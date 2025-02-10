package dabbadash.order.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String userPassword;
    private String fullName;
    private String phoneNumber;
    private String userAddress;
    private String userRole;
}
