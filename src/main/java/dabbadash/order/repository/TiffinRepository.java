package dabbadash.order.repository;


import dabbadash.order.entity.Tiffin;
import dabbadash.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TiffinRepository extends JpaRepository<Tiffin, Integer> {
    Tiffin findByTiffinName(String tiffinName);
    Tiffin findByTiffinId(int tiffinId);
    List<Tiffin> findByRestaurant(User restaurant);
    List<Tiffin> findByTiffinNameContaining(String tiffinName);
    List<Tiffin> findByTiffinType(Tiffin.TiffinType tiffinType);
}
