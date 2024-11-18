package com.springboot.obbm.respository;

import com.springboot.obbm.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Page<Menu> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Menu> findByMenuIdAndDeletedAtIsNull(int id);

    Optional<Menu> findTopByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(String users_userId);

    Page<Menu> findAllByUsers_UserIdAndDeletedAtIsNullOrderByCreatedAtDesc(String users_userId, Pageable pageable);

    Page<Menu> findAllByIsmanagedAndDeletedAtIsNull(boolean ismanaged, Pageable pageable);
}
