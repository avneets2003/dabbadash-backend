package dabbadash.order.repository;

import dabbadash.order.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.Tiffin;

public interface TiffinRepository extends JpaRepository<Tiffin, Integer> {
    Tiffin findById(int id);
    Tiffin findByTiffinName(String tiffinName);

    @Transactional
    void deleteByRestaurant(User restaurant);
}
