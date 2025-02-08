package dabbadash.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUserId(int userId);

}
