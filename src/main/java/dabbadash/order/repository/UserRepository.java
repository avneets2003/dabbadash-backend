package dabbadash.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
}
