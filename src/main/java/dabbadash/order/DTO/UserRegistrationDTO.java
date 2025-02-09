package dabbadash.order.DTO;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String email;
    private String userPassword;
    private String userConfirmPassword;
    private String fullName;
    private String phoneNumber;
    private String userAddress;
    private String userRole;
}
