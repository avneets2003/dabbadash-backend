package dabbadash.order.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userPhoneNumber;
    private String userAddress;
    private String userRole;
}
