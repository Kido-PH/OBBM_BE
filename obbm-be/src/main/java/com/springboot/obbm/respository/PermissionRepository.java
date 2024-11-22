package com.springboot.obbm.respository;

import com.springboot.obbm.model.Category;
import com.springboot.obbm.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Page<Category> findAllByDeletedAtIsNull(Pageable pageable);
}
