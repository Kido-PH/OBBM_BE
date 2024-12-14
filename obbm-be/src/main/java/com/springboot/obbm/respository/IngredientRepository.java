package com.springboot.obbm.respository;

import com.springboot.obbm.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
    Page<Ingredient> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    Optional<Ingredient> findByIngredientIdAndDeletedAtIsNull(int id);
}
