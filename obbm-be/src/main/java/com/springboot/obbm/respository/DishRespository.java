package com.springboot.obbm.respository;

import com.springboot.obbm.models.Category;
import com.springboot.obbm.models.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRespository extends JpaRepository<Dish, Integer> {
    Page<Dish> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Dish> findByDishIdAndDeletedAtIsNull(int id);
}