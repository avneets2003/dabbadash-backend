package dabbadash.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByuserEmail(String userEmail);
    Optional<User> findByuserPhoneNumber(String userPhoneNumber);
}
