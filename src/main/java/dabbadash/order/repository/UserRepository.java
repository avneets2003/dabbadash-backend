package dabbadash.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
