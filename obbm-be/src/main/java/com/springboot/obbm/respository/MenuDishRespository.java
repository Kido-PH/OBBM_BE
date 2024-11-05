package com.springboot.obbm.respository;

import com.springboot.obbm.model.MenuDish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuDishRespository extends JpaRepository<MenuDish, Integer> {
    Page<MenuDish> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<MenuDish> findByMenudishIdAndDeletedAtIsNull(int id);

    Page<MenuDish> findAllByMenus_MenuIdAndDeletedAtIsNull(int menuId, Pageable pageable);

    Page<MenuDish> findAllByDishes_DishIdAndDeletedAtIsNull(int dishId, Pageable pageable);
}
