package com.springboot.obbm.respository;

import com.springboot.obbm.models.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRespository extends JpaRepository<Menu, Integer> {
    Page<Menu> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Menu> findByMenuIdAndDeletedAtIsNull(int id);
}
