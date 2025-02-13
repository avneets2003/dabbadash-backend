package dabbadash.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dabbadash.order.entity.Tiffin;

public interface TiffinRepository extends JpaRepository<Tiffin, Integer> {
    Tiffin findById(int id);
    Tiffin findByTiffinName(String tiffinName);

}
