package com.springboot.obbm.respository;

import com.springboot.obbm.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    Optional<Category> findByCategoryIdAndDeletedAtIsNull(int id);
}
