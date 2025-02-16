package dabbadash.order.controller;

import dabbadash.order.DTO.UserDTO;
import dabbadash.order.DTO.UserDTOForUpdation;
import dabbadash.order.entity.User;
import dabbadash.order.repository.DeliveryStatusRepository;
import dabbadash.order.repository.OrderRepository;
import dabbadash.order.repository.TiffinRepository;
import dabbadash.order.repository.UserRepository;
import dabbadash.order.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TiffinRepository tiffinRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryStatusRepository deliveryStatusRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Extract email from JWT
            User user = userRepository.findByuserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserDTO userDTO = new UserDTO(
                    user.getUserEmail(),
                    user.getUserName(),
                    user.getUserPhoneNumber(),
                    user.getUserAddress(),
                    user.getUserRole()
            );
            return ResponseEntity.ok(userDTO);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("unexpected error ocurred while fetching user " + ex.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTOForUpdation userDTOForUpdation) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Extract email from JWT
            User user = userRepository.findByuserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if(userDTOForUpdation.getUserEmail()==null || userDTOForUpdation.getUserEmail().isEmpty()){
                return ResponseEntity.badRequest().body("Email is required");
            }
            if(userDTOForUpdation.getUserName()==null || userDTOForUpdation.getUserName().isEmpty()){
                return ResponseEntity.badRequest().body("Name is required, It can not be empty");
            }
            if(userDTOForUpdation.getUserAddress()==null || userDTOForUpdation.getUserAddress().isEmpty()){
                return ResponseEntity.badRequest().body("Address is required, It can not be empty");
            }
            user.setUserPhoneNumber(userDTOForUpdation.getUserEmail());
            user.setUserName(userDTOForUpdation.getUserName());
            user.setUserAddress(userDTOForUpdation.getUserAddress());
            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("unexpected error ocurred while updating user " + ex.getMessage());
        }
    }

    @Transactional // Do changes for MongoDB later
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Extract email from JWT

            User user = userRepository.findByuserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Delete all related records first
            orderRepository.deleteByCustomer(user);
            orderRepository.deleteByRestaurant(user);
            tiffinRepository.deleteByRestaurant(user);
            deliveryStatusRepository.deleteByDeliveryAgent(user);

            // Now delete the user
            userRepository.delete(user);

            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred while deleting user: " + ex.getMessage());
        }
    }
}
