package dabbadash.order.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
// Defining fields which are modifiable for user after account creation
public class UserDTOForUpdation {
    private String userEmail;
    private String userName;
    private String userAddress;
}
