package com.springboot.obbm.respository;

import com.springboot.obbm.model.Dish;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
    Page<Dish> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    Optional<Dish> findByDishIdAndDeletedAtIsNull(int id);

    @Query("SELECT d FROM dish d JOIN d.categories c JOIN d.event e WHERE e.eventId = :eventId")
    List<Dish> findByEventId(@Param("eventId") Integer eventId);
}
