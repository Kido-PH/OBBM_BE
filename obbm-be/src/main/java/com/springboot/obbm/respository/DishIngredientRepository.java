package com.springboot.obbm.respository;

import com.springboot.obbm.model.DishIngredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishIngredientRepository extends JpaRepository<DishIngredient, Integer> {
    Page<DishIngredient> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<DishIngredient> findByDishingredientIdAndDeletedAtIsNull(int id);

    Page<DishIngredient> findAllByDishes_DishIdAndDeletedAtIsNull(int dishId, Pageable pageable);

    Page<DishIngredient> findAllByIngredients_IngredientIdAndDeletedAtIsNull(int ingredientId, Pageable pageable);
}
