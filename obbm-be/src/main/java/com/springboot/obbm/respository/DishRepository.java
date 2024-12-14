package com.springboot.obbm.respository;

import com.springboot.obbm.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
    Page<Dish> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    Optional<Dish> findByDishIdAndDeletedAtIsNull(int id);
}
